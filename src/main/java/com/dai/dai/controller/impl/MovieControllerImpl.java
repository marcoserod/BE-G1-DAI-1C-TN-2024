package com.dai.dai.controller.impl;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.controller.MovieController;
import com.dai.dai.dto.movie.response.*;
import com.dai.dai.exception.DaiException;
import com.dai.dai.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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


@AllArgsConstructor
@RestController
@Tag(name = "Movie Controller", description = "Endpoints for operations related to TMDB API.")
@RequestMapping("/movies")
public class MovieControllerImpl implements MovieController {

    MovieService movieService;


    @Operation(summary = "It returns a list of popular movies, allowing users to easily access trending content.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Movie.class)))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/popular")
    @Override
    public ResponseEntity<GetMoviesResponse> getPopularMovies(
            @RequestParam(value = "page") Integer page,
            @RequestHeader(name = "Authorization") String accessToken
    ) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getPopularMovies(page), HttpStatus.OK);
    }

    @Operation(summary = "It returns a list of movies currently in theaters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Movie.class)))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/now_playing")
    @Override
    public ResponseEntity<GetMoviesResponse> getNowPlayingMovies(
            @RequestParam(value = "page") Integer page,
            @RequestHeader(name = "Authorization") String accessToken
    ) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getNowPlayingMovies(page), HttpStatus.OK);
    }

    @Operation(summary = "It returns the details of the requested movie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GetMovieDetailsResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "404", description = "The requested movie was not found",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/{movieId}")
    @Override
    public ResponseEntity<GetMovieDetailsResponse> getMovieById(
            @Valid @PathVariable(value = "movieId" ) Integer movieId,
            @RequestHeader(name = "Authorization") String accessToken) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getMovieById(movieId), HttpStatus.OK);
    }

    @Operation(summary = "It returns the available movie genres.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GetAvailableMovieGenresResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/genre/list")
    @Override
    public ResponseEntity<GetAvailableMovieGenresResponse> getAvailableMovieGenres(
            @RequestHeader(name = "Authorization") String accessToken) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getAvailableMovieGenres() ,HttpStatus.OK);
    }

    @Operation(summary = "It returns a list of movies based on their original titles, translated titles, " +
            "alternative titles, or cast member names.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GetMoviesResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "404", description = "Movies not found",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping(value = "/search")
    @Override
    public ResponseEntity<GetMoviesResponse> getMoviesByName(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "page") Integer page,
            @RequestHeader(name = "Authorization") String accessToken) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getMoviesByName(name), HttpStatus.OK);
    }
}
