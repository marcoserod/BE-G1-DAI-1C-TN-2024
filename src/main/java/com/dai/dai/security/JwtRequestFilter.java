package com.dai.dai.security;

import com.dai.dai.entity.SessionEntity;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

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

                Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(authorizationHeader)
                        .getBody();
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
                    throw new Exception("Unauthorized.");
                }




        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid or expired token");
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred.");

        }

        chain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/auth") || path.equals("/auth/refreshToken")  || path.equals("/api-docs ");
    }

}
