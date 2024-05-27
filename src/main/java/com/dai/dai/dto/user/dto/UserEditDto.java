package com.dai.dai.dto.user.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class UserEditDto {

    private String name;
    private String surname;
    private String nickname;
    private MultipartFile profileImage;
}
