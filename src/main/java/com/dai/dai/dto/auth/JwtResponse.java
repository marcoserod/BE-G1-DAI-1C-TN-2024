package com.dai.dai.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    Integer userId;
    String moviePlayToken;
    String refreshToken;
}
