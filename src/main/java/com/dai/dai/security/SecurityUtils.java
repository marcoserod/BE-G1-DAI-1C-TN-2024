package com.dai.dai.security;

import com.dai.dai.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Slf4j
@Service
public class SecurityUtils {

    @Value("${jwt.client.secret}")
    String secretKey;

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getUserIdFromToken(String accessToken){
        Claims claims = null;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        var authorizationHeaderSubstring = accessToken.substring(7);
        claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authorizationHeaderSubstring)
                .getBody();
        var email = claims.getSubject();
        var user = userRepository.findByEmail(email);
        return user.getId().longValue();
    }
}
