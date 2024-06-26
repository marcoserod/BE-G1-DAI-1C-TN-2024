package com.dai.dai.controller.impl;

import com.dai.dai.controller.UserController;
import com.dai.dai.dto.movie.response.GetAvailableMovieGenresResponse;
import com.dai.dai.dto.movie.response.GetFavoriteMoviesResponse;
import com.dai.dai.dto.movie.response.GetMoviesResponse;
import com.dai.dai.dto.user.PostUsersResponse;
import com.dai.dai.dto.user.dto.FilmRatingDto;
import com.dai.dai.dto.user.dto.UserDto;
import com.dai.dai.dto.user.dto.UserEditDto;
import com.dai.dai.exception.DaiException;
import com.dai.dai.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@AllArgsConstructor
@RestController
@Tag(name = "User Controller", description = "Endpoints for user-related operations.")
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    UserService userService;
    @Operation(summary = "It gets user info for a given user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @GetMapping("{userId}")
    @Override
    public ResponseEntity<UserDto> getUserInfoById(@Valid @PathVariable(value = "userId" ) Integer userId,
                                                   @Parameter(name = "Authorization", description = "Bearer token",
                                                           required = true, in = ParameterIn.HEADER,
                                                           schema = @Schema(type = "string", format = "Bearer"))
                                                   @RequestHeader(name = "Authorization") String accessToken) {
        return new ResponseEntity<>(userService.getUserInfoById(userId), HttpStatus.OK);
    }


    @Operation(summary = "It adds film to user favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Film added to favorites."),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie not found.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @PostMapping("/{userId}/favorites/{filmId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addFavorite(@Valid @PathVariable(value = "userId" ) Integer userId,
                                            @Valid @PathVariable(value = "filmId" ) Integer filmId,
                                            @Parameter(name = "Authorization", description = "Bearer token",
                                                    required = true, in = ParameterIn.HEADER,
                                                    schema = @Schema(type = "string", format = "Bearer"))
                                            @RequestHeader(name = "Authorization") String accessToken)
            throws IOException, InterruptedException {
        userService.addFavorite(userId, filmId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "It returns the movies from the user favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Film favourites found.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetFavoriteMoviesResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @GetMapping("/{userId}/favorites")
    @Override
    public ResponseEntity<GetFavoriteMoviesResponse> getFavorites(
            @Valid @PathVariable(value = "userId" ) Integer userId,
            @Parameter(name = "Authorization", description = "Bearer token",
                    required = true, in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", format = "Bearer"))
            @RequestHeader(name = "Authorization") String accessToken,
            @RequestParam(value = "page") Integer page) throws IOException, InterruptedException {
        return new ResponseEntity<>(userService.getFavorites(userId, page), HttpStatus.OK);
    }

    @Operation(summary = "It removes a movie from the user favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Film favorite removed."),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "404", description = "Movie is not in favorites.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @DeleteMapping("/{userId}/favorites/{filmId}")
    @Override
    public ResponseEntity<Void> removeFavorite(@Valid @PathVariable(value = "userId" ) Integer userId,
                                               @Valid @PathVariable(value = "filmId" ) Integer filmId,
                                               @Parameter(name = "Authorization", description = "Bearer token",
                                                       required = true, in = ParameterIn.HEADER,
                                                       schema = @Schema(type = "string", format = "Bearer"))
                                               @RequestHeader(name = "Authorization") String accessToken)
            throws IOException, InterruptedException {
        userService.removeFavorite(userId, filmId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "It deletes user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User removed."),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @DeleteMapping("{userId}")
    @Override
    public ResponseEntity<Void> removeUser(@Valid @PathVariable(value = "userId" ) Integer userId,
                                           @Parameter(name = "Authorization", description = "Bearer token",
                                                   required = true, in = ParameterIn.HEADER,
                                                   schema = @Schema(type = "string", format = "Bearer"))
                                           @RequestHeader(name = "Authorization") String accessToken)
            throws IOException, InterruptedException {
        userService.removeUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "It updates an existing user with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = PostUsersResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "409", description = "Existing nickname.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) })
    })
    @PatchMapping({"/{userId}"})
    @Override
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable(value = "userId" ) Integer userId,
                                              @RequestPart(required = false, name = "name") String name,
                                              @RequestPart(required = false, name = "surname") String surname,
                                              @RequestPart(required = false, name = "nickname") String nickname,
                                              @RequestPart(required = false, name = "profileImage") MultipartFile file,
                                              @Parameter(name = "Authorization", description = "Bearer token",
                                                      required = true, in = ParameterIn.HEADER,
                                                      schema = @Schema(type = "string", format = "Bearer"))
                                              @RequestHeader(name = "Authorization") String accessToken) throws IOException {

        return new ResponseEntity<>(userService.updateUser(name, surname, nickname, file, userId), HttpStatus.OK);
    }

}
