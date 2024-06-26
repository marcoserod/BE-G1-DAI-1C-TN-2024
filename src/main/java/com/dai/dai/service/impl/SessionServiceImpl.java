package com.dai.dai.service.impl;

import com.dai.dai.dto.auth.JwtResponse;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.entity.SessionEntity;
import com.dai.dai.entity.UserEntity;
import com.dai.dai.exception.InternalServerErrorException;
import com.dai.dai.exception.UnauthorizedException;
import com.dai.dai.repository.SessionRepository;
import com.dai.dai.repository.UserRepository;
import com.dai.dai.service.SessionService;
import com.dai.dai.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.awt.desktop.UserSessionEvent;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
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

    @Value("${jwt.client.secret}")
    String secretKey;

    private final UserRepository userRepository;
    private final UserService userService;
    private final SessionRepository sessionRepository;
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final NetHttpTransport transport = new NetHttpTransport();


    public JwtResponse generateToken(String authenticationRequest) throws Exception {
        GoogleIdToken googleToken = null;
        log.info("[generateToken] Validando el Google Auth Token...");
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, JSON_FACTORY)
                    // Reemplazar con clientId de las credenciales seteadas en el front
                    .setAudience(Arrays.asList(clientId))
                    .build();
            googleToken = verifier.verify(authenticationRequest);
        } catch (Exception e) {
            log.error("An error occurred while validating the GoogleAuth Token.");
            throw new InternalServerErrorException("An error occurred while validating the GoogleAuth Token.");
        }

        if (googleToken == null) {
            log.error("The Google Auth Token entered is not valid.");
            throw new UnauthorizedException("The Google Auth Token entered is not valid.");
        }
        log.error("The received Google Auth Token is valid.");

        var payload = googleToken.getPayload();

        // Get profile information from payload
        var email = payload.getEmail();
        var pictureUrl = (String) payload.get("picture");
        var surname = (String) payload.get("family_name");
        var name = (String) payload.get("given_name");
        UserEntity user = null;
        try {
            user = userRepository.findByEmail(email);
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while querying the database.");
        }
        Integer userId;
        String moviePlayToken;
        String refreshToken;
        if (user == null) {
            log.info("[generateToken] The user with email: {} is not registered, registration process begins.",email);
            var randomString = UUID.randomUUID().toString();
            var nickname = name + randomString.substring(0, 4);
            try {
                userId = userService.createUser(UserDto.builder().email(email)
                        .name(name)
                        .nickname(nickname)
                        .profileImage(pictureUrl)
                        .surname(surname).build());
            } catch (Exception e) {
                log.error("An error occurred while creating the user.");
                throw new InternalServerErrorException("An error occurred while creating the user.");
            }
            moviePlayToken = createJwtToken(email);
            refreshToken = createRefreshToken(email);
            log.info("Generating session...");
            SessionEntity sessionEntity = new SessionEntity();
            sessionEntity.setToken(moviePlayToken);
            sessionEntity.setRefreshToken(refreshToken);
            sessionEntity.setUserEmail(email);
            try {
                sessionRepository.save(sessionEntity);
            } catch (Exception e) {
                throw new InternalServerErrorException("An error occurred while persisting the session in the database.");
            }
        } else {
            log.info("[generateToken] Initiating session for the user with email: {}.",email);
            userId = user.getId();
            Optional<SessionEntity> userSession;
            try {
                userSession = sessionRepository.findByUserEmail(user.getEmail());
            } catch (Exception e) {
                log.error("An error occurred while retrieving the session from the database.");
                throw new InternalServerErrorException("An error occurred while retrieving the session from the database.");
            }
            if (userSession.isEmpty()){
                log.error("No session found for the user with email: {}.",email);
                throw new Exception("No session found for the user.");
            } else {
                SessionEntity newLoginSession = userSession.get();
                //Con el nuevo login se genera un nuevo moviePlayToken. Se mantiene el refreshToken.
                moviePlayToken = createJwtToken(email);
                refreshToken = userSession.get().getRefreshToken();
                newLoginSession.setToken(moviePlayToken);
                try {
                    //Se updatea la session de ese usuario.
                    sessionRepository.save(userSession.get());
                } catch (Exception e) {
                    log.error("An error occurred while persisting the session in the database.");
                    throw new InternalServerErrorException("An error occurred while persisting the session in the database.");
                }
            }
        }

        var authResponse = JwtResponse.builder()
                .userId(userId)
                .moviePlayToken(moviePlayToken)
                .refreshToken(refreshToken)
                .build();
        log.info("Flow ends. Tokens generated successfully.");
        return authResponse;

    }

    @Override
    public JwtResponse refreshToken(String refreshToken) throws Exception {
        Claims userInfo = decodeToken(refreshToken);
        log.info("[refreshToken] Session updated for the user with email: {}",userInfo.getSubject());
        Optional<SessionEntity> sessionOptional;
        try{
            sessionOptional = sessionRepository.findByUserEmail(userInfo.getSubject());
        } catch (Exception e) {
            log.error("An error occurred while querying the database.");
            throw new InternalServerErrorException("An error occurred while querying the database.");
        }
        if (sessionOptional.isEmpty()){
            log.error("UserSession Not Found.");
            throw new Exception("UserSession Not Found");
        }
        SessionEntity session = sessionOptional.get();
        session.setToken(createJwtToken(session.getUserEmail()));
        try {
            //Se updatea la session de ese usuario.
            sessionRepository.save(session);
        } catch (Exception e ){
            throw new InternalServerErrorException("An error occurred while persisting the session in the database.");
        }
        log.info("Session token: {}", session.getToken());
        return JwtResponse.builder()
                .refreshToken(session.getRefreshToken())
                .moviePlayToken(session.getToken())
                .build();
    }

    @Override
    public void logout(String refreshToken) throws Exception {
        Claims userInfo = decodeToken(refreshToken);
        log.info("The session for the user with email: {} has been closed.",userInfo.getSubject());
        Optional<SessionEntity> session;
        try{
            session = sessionRepository.findByUserEmail(userInfo.getSubject());
        } catch (Exception e) {
            log.error("An error occurred while querying the database.");
            throw new InternalServerErrorException("An error occurred while querying the database.");
        }
        if (session.isPresent()){
            var loggedOutToken = Jwts.builder()
                    .setSubject(userInfo.getSubject())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date())
                    .claim("isActive", Boolean.FALSE)
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), HS512)
                    .compact();
            var loggedOutSession = session.get();
            loggedOutSession.setToken(loggedOutToken);
            try {
                sessionRepository.save(loggedOutSession);
            } catch (Exception e ){
                throw new InternalServerErrorException("An error occurred while closing the session.");
            }
        } else {
            log.error("Session not found.");
            throw new InternalServerErrorException("Session not found.");
        }
        log.info("The session was closed successfully.");
    }


    private String createJwtToken(String email) {

        Date issuedAt = new Date();

        Date expirationToken = getDateOneHourFromNow();

        var moviePlayToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationToken)
                .claim("isActive", Boolean.TRUE)
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

    private Claims decodeToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return jwsClaims.getBody();
    }



    //Genera una hora de apartir de este instante. Se usa para generar el nuevo expire time del token.
    private Date getDateOneHourFromNow(){
        // Obtén el instante actual
        Instant now = Instant.now();

        // Agrega una hora al instante actual
        Instant oneHourLater = now.plus(Duration.ofHours(1));

        // Obtén los milisegundos desde la época (1970-01-01T00:00:00Z)
        long oneHourLaterMillis = oneHourLater.toEpochMilli();

        return new Date(oneHourLaterMillis);
    }
}
