package com.dai.dai.controller.impl;

import com.dai.dai.controller.UserController;
import com.dai.dai.dto.movie.GetMovieTrailerDetailsResponse;
import com.dai.dai.dto.user.PostUsersResponse;
import com.dai.dai.dto.user.UserDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;


@AllArgsConstructor
@RestController
@Tag(name = "User Controller", description = "Endpoints for user-related operations")
@RequestMapping("/users")
@Validated
public class UserControllerImpl implements UserController {

    UserService userService;

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
            @ApiResponse(responseCode = "409", description = "Existing nickname",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), CREATED);
    }
}
