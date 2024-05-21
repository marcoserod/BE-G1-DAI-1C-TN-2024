package com.dai.dai.service;

import com.dai.dai.dto.auth.JwtResponse;

import java.io.IOException;

public interface SessionService {

    JwtResponse generateToken(String authenticationRequest) throws Exception;

    JwtResponse refreshToken(String refreshToken) throws Exception;
    void logout(String refreshToken) throws Exception;


}
