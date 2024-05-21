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
import java.io.IOException;

public interface AuthController {

    ResponseEntity<JwtResponse> login(String authenticationRequest) throws Exception;

    ResponseEntity<JwtResponse> refreshToken(String refreshToken) throws Exception;

    void logout(String refreshToken) throws Exception;
}
