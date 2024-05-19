package com.dai.dai.controller;

import com.dai.dai.dto.auth.JwtResponse;
import com.dai.dai.exception.DaiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.AuthenticationException;

public interface AuthController {

    @Operation(summary = "It initiates the authentication process using Google as the identity provider." +
            " The user will be redirected to the Google login page.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @PostMapping()
    ResponseEntity<JwtResponse> login(String authenticationRequest) throws AuthenticationException;

    String refreshToken(String refreshToken) throws AuthenticationException;

    void logout(String refreshToken) throws AuthenticationException;
}
