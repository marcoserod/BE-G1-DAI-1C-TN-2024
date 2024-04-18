package com.dai.dai.service;

import com.dai.dai.dto.movie.response.GetMoviesResponse;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.dto.user.dto.UserFavoriteDto;

import java.io.IOException;

public interface UserService {
    UserDto getUserInfoById(Integer userID);

    UserDto createUser(UserDto userDto);

    void addFavorite(UserFavoriteDto userFavoriteDto) throws IOException, InterruptedException;

    GetMoviesResponse getFavorites(Integer userID) throws IOException, InterruptedException;

    void removeFavorite(UserFavoriteDto userFavoriteDto) throws IOException, InterruptedException;

    void removeUser(Integer userID) throws IOException, InterruptedException;
}
