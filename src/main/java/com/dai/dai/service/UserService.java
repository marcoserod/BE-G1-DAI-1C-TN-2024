package com.dai.dai.service;

import com.dai.dai.dto.movie.response.GetFavoriteMoviesResponse;
import com.dai.dai.dto.movie.response.GetMoviesResponse;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.dto.user.dto.UserEditDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserDto getUserInfoById(Integer userID);

    Integer createUser(UserDto userDto) throws IOException;

    void addFavorite(Integer userId, Integer filmId) throws IOException, InterruptedException;

    GetFavoriteMoviesResponse getFavorites(Integer userID) throws IOException, InterruptedException;

    void removeFavorite(Integer userId, Integer filmId) throws IOException, InterruptedException;

    void removeUser(Integer userID) throws IOException, InterruptedException;

    UserDto updateUser(String name, String surname, String nickname, MultipartFile file, Integer userId) throws IOException;
}
