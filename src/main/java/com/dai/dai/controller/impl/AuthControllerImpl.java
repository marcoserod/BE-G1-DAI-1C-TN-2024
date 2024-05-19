package com.dai.dai.controller.impl;

import com.dai.dai.controller.AuthController;
import com.dai.dai.dto.auth.JwtResponse;
import com.dai.dai.exception.DaiException;
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

import javax.naming.AuthenticationException;

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
            @ApiResponse(responseCode = "200", description = "Logged in"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @PostMapping()
    @Override
    public ResponseEntity<JwtResponse> login(String authenticationRequest) {
        try {
            var jwtResponse = sessionService.generateToken(authenticationRequest);
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(UNAUTHORIZED, "Unauthorized");
        }
    }

    @Operation(summary = "It requests a new access token using a previously " +
            "obtained refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token generated successfully.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class)) }),
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
    public String refreshToken(@RequestParam("refreshToken") String refreshToken) throws AuthenticationException {
        return null;
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
    public void logout(@RequestHeader(name = "Authorization") String accessToken) {

    }
}
