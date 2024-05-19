package com.dai.dai.service;

import com.dai.dai.dto.auth.JwtResponse;

public interface SessionService {

    JwtResponse generateToken(String authenticationRequest);
}
