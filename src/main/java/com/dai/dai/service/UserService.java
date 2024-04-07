package com.dai.dai.service;

import com.dai.dai.dto.user.UserDto;

public interface UserService {
    UserDto getUserInfoById(Integer userID);

    UserDto createUser(UserDto userDto);
}
