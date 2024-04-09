package com.dai.dai.controller;

import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.dto.user.dto.UserFavoriteDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface UserController {
    ResponseEntity<UserDto> getUserInfoById(Integer userID);

    ResponseEntity<UserDto> createUser(UserDto userDto);

    ResponseEntity<Void> addFavorite(UserFavoriteDto userFavoriteDto) throws IOException, InterruptedException;
}
