package com.dai.dai.controller.impl;

import com.dai.dai.controller.UserController;
import com.dai.dai.dto.user.PostUsersResponse;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.dto.user.dto.UserFavoriteDto;
import com.dai.dai.exception.DaiException;
import com.dai.dai.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;


@AllArgsConstructor
@RestController
@Tag(name = "User Controller", description = "Endpoints for user-related operations")
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    UserService userService;
    @Operation(summary = "Get user info for a given user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @GetMapping("/details/{user_id}")
    @Override
    public ResponseEntity<UserDto> getUserInfoById(@Valid @PathVariable(value = "user_id" ) Integer userId) {
        return new ResponseEntity<>(userService.getUserInfoById(userId), HttpStatus.OK);
    }


    @Operation(summary = "Create a new user with the data obtained from the call")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = PostUsersResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "409", description = "Existing nickname",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), CREATED);
    }

    @Operation(summary = "Add film to user favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Film added to favorites."),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "409", description = "User not found",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @PostMapping("/favorites")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addFavorite(@RequestBody UserFavoriteDto userFavoriteDto) throws IOException,
            InterruptedException {
        userService.addFavorite(userFavoriteDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
