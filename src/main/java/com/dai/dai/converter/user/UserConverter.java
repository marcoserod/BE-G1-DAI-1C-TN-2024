package com.dai.dai.converter.user;

import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.entity.UserEntity;

public class UserConverter {

    public static UserDto fromUserEntityToUserDto(UserEntity userEntity){
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .surname(userEntity.getSurname())
                .profileImage(userEntity.getProfile_image())
                .build();
    }
}
