package com.dai.dai.controller.impl;

import com.dai.dai.controller.AuthController;
import com.dai.dai.exception.DaiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.net.URI;

@AllArgsConstructor
@Tag(name = "Auth Controller", description = "Controller responsible for handling authentication operations.")
@RequestMapping("/auth")
@RestController
public class AuthControllerImpl implements AuthController {
    @Operation(summary = "This endpoint initiates the authentication process using Google as the identity provider." +
            " The user will be redirected to the Google login page.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Found. (Redirection to Google Single Sign On.)"),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @PostMapping(value = "/google")
    @Override
    public URI initGoogleAuth() throws AuthenticationException {
        return null;
    }

    @Operation(summary = "This endpoint handles the response from Google after the user has logged in. Here, " +
            "the received authorization code from Google is verified and processed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Found. (Redirection from Google Single Sign On.)"),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping(value = "/google/callback")
    @Override
    public void handleGoogleCallback(@RequestParam("code")String code) throws AuthenticationException {

    }

    @Operation(summary = "This endpoint is used to request a new access token using a previously " +
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
}
