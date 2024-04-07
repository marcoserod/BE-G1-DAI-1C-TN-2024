package com.dai.dai.converter.user;

import com.dai.dai.dto.user.UserDto;
import com.dai.dai.entity.UserEntity;

public class UserConverter {

    public static UserDto fromUserEntityToUserDto(UserEntity userEntity){
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .profile_image(userEntity.getProfile_image())
                .build();
    }
}
