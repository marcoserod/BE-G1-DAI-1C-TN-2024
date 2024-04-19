package com.dai.dai.controller;

import javax.naming.AuthenticationException;
import java.net.URI;

public interface AuthController {

    URI initGoogleAuth() throws AuthenticationException;

    String refreshToken(String refreshToken) throws AuthenticationException;

    void logout(String refreshToken) throws AuthenticationException;
}
