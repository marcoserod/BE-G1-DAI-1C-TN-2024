package com.dai.dai.controller;

import com.dai.dai.dto.movie.response.GetMoviesResponse;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.dto.user.dto.UserEditDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserController {
    ResponseEntity<UserDto> getUserInfoById(Integer userID, String accessToken);

    ResponseEntity<Void> addFavorite(Integer userId, Integer filmId, String accessToken) throws IOException,
            InterruptedException;

    ResponseEntity<GetMoviesResponse> getFavorites(Integer userId, String accessToken, Integer page)
            throws IOException, InterruptedException;

    ResponseEntity<Void> removeFavorite(Integer userId, Integer filmId, String accessToken) throws IOException,
            InterruptedException;

    ResponseEntity<Void> removeUser(Integer userID, String accessToken) throws IOException, InterruptedException;

    ResponseEntity<UserDto> updateUser(Integer userId, String name, String surname, String nickname,
                                       MultipartFile file, String accessToken)
            throws IOException, InterruptedException;
}
