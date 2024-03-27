package com.dai.dai.converter.user;

import com.dai.dai.controller.UserController;
import com.dai.dai.dto.user.UserDto;
import com.dai.dai.entity.UserEntity;

public class UserConverter {

    public static UserDto fromUserEntityToUserDto(UserEntity userEntity){
        return UserDto.builder()
                .id(userEntity.getId())
                .uuid(userEntity.getUuid())
                .email(userEntity.getEmail())
                .nombre(userEntity.getNombre())
                .apellido(userEntity.getApellido())
                .imagenPerfil(userEntity.getImagenPerfil())
                .build();
    }
}
