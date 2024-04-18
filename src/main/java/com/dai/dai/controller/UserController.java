package com.dai.dai.controller;

import com.dai.dai.dto.movie.response.GetMoviesResponse;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.dto.user.dto.UserFavoriteDto;
import com.dai.dai.exception.DaiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

public interface UserController {
    ResponseEntity<UserDto> getUserInfoById(Integer userID);

    ResponseEntity<UserDto> createUser(UserDto userDto);

    ResponseEntity<Void> addFavorite(UserFavoriteDto userFavoriteDto) throws IOException, InterruptedException;

    ResponseEntity<GetMoviesResponse> getFavorites(Integer userId) throws IOException, InterruptedException;

    ResponseEntity<Void> removeFavorite(UserFavoriteDto userFavoriteDto) throws IOException,
            InterruptedException;

    ResponseEntity<Void> removeUser(Integer userID) throws IOException, InterruptedException;
}
