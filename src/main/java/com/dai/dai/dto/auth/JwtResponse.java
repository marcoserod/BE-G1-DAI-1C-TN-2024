package com.dai.dai.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer userId;
    String moviePlayToken;
    String refreshToken;
}
