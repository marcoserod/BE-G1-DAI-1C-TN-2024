package com.dai.dai.controller.impl;

import com.dai.dai.controller.AuthController;
import com.dai.dai.dto.auth.JwtResponse;
import com.dai.dai.dto.auth.request.AuthRequest;
import com.dai.dai.dto.auth.request.LogOutRequest;
import com.dai.dai.dto.auth.request.RefreshTokenRequest;
import com.dai.dai.exception.DaiException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dai.dai.service.impl.SessionServiceImpl;

import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;

import java.io.IOException;
import java.util.Base64;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@AllArgsConstructor
@Tag(name = "Auth Controller", description = "Endpoints for handling authentication operations.")
@RequestMapping("/auth")
@RestController
public class AuthControllerImpl implements AuthController {

    private final SessionServiceImpl sessionService;

    @Operation(summary = "It initiates the authentication process using Google as the identity provider." +
            " The user will be redirected to the Google login page.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JwtResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @PostMapping()
    @Override
    public ResponseEntity<JwtResponse> login(@RequestBody AuthRequest authenticationRequest) throws Exception {
            return new ResponseEntity<>(sessionService.generateToken(authenticationRequest.getAuthToken()), HttpStatus.OK);

    }

    @Operation(summary = "It requests a new access token using a previously " +
            "obtained refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token generated successfully.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JwtResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @PostMapping(value = "/refreshToken")
    @Override
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) throws Exception {
        return new ResponseEntity<>(sessionService.refreshToken(refreshToken.getRefreshToken()), HttpStatus.OK);

    }

    @Operation(summary = "It logs the user out by invalidating the current session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful."),
            @ApiResponse(responseCode = "401", description = "The user is not logged in."),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @DeleteMapping
    @Override
    public void logout(@RequestBody LogOutRequest refreshToken) throws Exception {
        sessionService.logout(refreshToken.getRefreshToken());
    }
}
