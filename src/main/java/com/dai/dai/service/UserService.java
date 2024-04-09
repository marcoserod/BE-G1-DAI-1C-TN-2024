package com.dai.dai.service;

import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.dto.user.dto.UserFavoriteDto;
import com.dai.dai.entity.UserFavoriteEntity;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDto getUserInfoById(Integer userID);

    UserDto createUser(UserDto userDto);

    void addFavorite(UserFavoriteDto userFavoriteDto) throws IOException, InterruptedException;
}
