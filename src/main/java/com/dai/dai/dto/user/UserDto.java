package com.dai.dai.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {

    private int id;
    private String email;
    private String name;
    private String surname;
    private String nickname;
    private String profile_image;
}
