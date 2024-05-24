package com.dai.dai.security;

import com.dai.dai.exception.InternalServerErrorException;
import com.dai.dai.exception.UnauthorizedException;
import com.dai.dai.repository.SessionRepository;
import com.dai.dai.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.Key;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    @Value("${jwt.client.secret}")
    String secretKey;

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            final String authorizationHeader = request.getHeader("Authorization");
            Claims claims = null;
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            if (authorizationHeader != null && authorizationHeader.toLowerCase().startsWith("bearer ")) {
                var authorizationHeaderSubstring = authorizationHeader.substring(7);
                claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(authorizationHeaderSubstring)
                        .getBody();
            } else {
                log.error("El header Authentication no contiene un bearer token valido.");
                throw new UnauthorizedException("El header Authentication no contiene un bearer token valido.");
            }
            var email = claims.getSubject();
            var userDetails = userRepository.findByEmail(email);
            var userSession = sessionRepository.findByUserEmail(email);

            //Chequea si tiene una sesión en la bbdd, si la sesión no está vencida y si esta Activa.
            if (userSession.isPresent() ){
                var sessionToken = userSession.get().getToken();
                Claims sessionTokenClaims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(sessionToken)
                        .getBody();
                var isActive = sessionTokenClaims.get("isActive", Boolean.class);
                if ( isActive ){
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null,null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                log.error("El usuario no posee una sesión activa.");
                throw new UnauthorizedException("El usuario no posee una sesión activa.");
            }




        } catch (JwtException | IllegalArgumentException e) {
            log.error("El token es invalido o está expirado.");
            throw new UnauthorizedException("El token es invalido o está expirado.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Ocurrió un Internal Server Error.");
        }

        chain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/auth") || path.equals("/auth/refreshToken")  || path.equals("/api-docs");
    }

}
