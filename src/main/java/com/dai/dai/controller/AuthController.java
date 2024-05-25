package com.dai.dai.controller;

import com.dai.dai.dto.auth.JwtResponse;
import com.dai.dai.dto.auth.request.AuthRequest;
import com.dai.dai.dto.auth.request.LogOutRequest;
import com.dai.dai.dto.auth.request.RefreshTokenRequest;
import com.dai.dai.exception.DaiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.AuthenticationException;
import java.io.IOException;

public interface AuthController {

    ResponseEntity<JwtResponse> login(AuthRequest authenticationRequest) throws Exception;

    ResponseEntity<JwtResponse> refreshToken(RefreshTokenRequest refreshToken) throws Exception;

    void logout(LogOutRequest refreshToken) throws Exception;
}
