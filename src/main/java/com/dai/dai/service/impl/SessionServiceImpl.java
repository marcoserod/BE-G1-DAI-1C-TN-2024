package com.dai.dai.service.impl;

import com.dai.dai.dto.auth.JwtResponse;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.entity.SessionEntity;
import com.dai.dai.repository.SessionRepository;
import com.dai.dai.repository.UserRepository;
import com.dai.dai.service.SessionService;
import com.dai.dai.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {

    @Value("${google.clientId}")
    String clientId;

    private static final String secretKey = "A5C4C46BD6CCC1396B3DEFED617339E9EBD4A8EA4B0CA3EE81FCA4FC2FBB26D1";

    private final UserRepository userRepository;
    private final UserService userService;
    private final SessionRepository sessionRepository;
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final NetHttpTransport transport = new NetHttpTransport();


    public JwtResponse generateToken(String authenticationRequest) {

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, JSON_FACTORY)
                    // Reemplazar con clientId de las credenciales seteadas en el front
                    .setAudience(Arrays.asList(clientId))
                    .build();

            GoogleIdToken googleToken = verifier.verify(authenticationRequest);
            if (googleToken == null) {
                throw new ResponseStatusException(UNAUTHORIZED, "Unauthorized");
            }

            var payload = googleToken.getPayload();

            // Get profile information from payload
            var email = payload.getEmail();
            var pictureUrl = (String) payload.get("picture");
            var surname = (String) payload.get("family_name");
            var name = (String) payload.get("given_name");

            var user = userRepository.findByEmail(email);
            Integer userId;
            if (user == null) {
                var randomString = UUID.randomUUID().toString();
                var nickname = surname + randomString.substring(0, 4);
                userId = userService.createUser(UserDto.builder().email(email)
                        .name(name)
                        .nickname(nickname)
                        .profileImage(pictureUrl)
                        .surname(surname).build());
            } else {
                userId = user.getId();
            }

            var moviePlayToken = createJwtToken(email);
            var refreshToken = createRefreshToken(email);

            SessionEntity sessionEntity = new SessionEntity();
            sessionEntity.setToken(moviePlayToken);
            sessionEntity.setRefreshToken(refreshToken);
            sessionEntity.setUserEmail(email);
            sessionRepository.save(sessionEntity);

            var authResponse = JwtResponse.builder()
                    .userId(userId)
                    .moviePlayToken(moviePlayToken)
                    .refreshToken(refreshToken)
                    .build();
            return authResponse;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    private String createJwtToken(String email) {

        Date issuedAt = new Date();

        Date expirationToken = new Date(issuedAt.getTime() + 3600000); // 1 hora en milisegundos

        var moviePlayToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationToken)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), HS512)
                .compact();

        return moviePlayToken;
    }

    private String createRefreshToken(String email) {

        Date issuedAt = new Date();

        Date expirationRefreshToken = new Date(issuedAt.getTime() + 365L * 24 * 60 * 60 * 1000 ); // 1 año en milisegundos

        var refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationRefreshToken)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), HS512)
                .compact();

        return refreshToken;
    }
}
