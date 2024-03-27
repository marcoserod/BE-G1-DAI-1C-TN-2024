package com.dai.dai.controller;

import com.dai.dai.dto.user.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserController {
    ResponseEntity<UserDto> getUserInfoById(Integer userID);
}
