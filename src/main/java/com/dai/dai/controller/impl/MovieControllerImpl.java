package com.dai.dai.controller.impl;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.controller.MovieController;
import com.dai.dai.dto.movie.response.*;
import com.dai.dai.exception.DaiException;
import com.dai.dai.service.MovieService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@AllArgsConstructor
@RestController
@Tag(name = "Movie Controller", description = "Endpoints for operations related to TMDB API.")
@RequestMapping("/movies")
public class MovieControllerImpl implements MovieController {

    MovieService movieService;

    @Operation(summary = "It returns a list of movies currently in theaters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetMoviesResponse.class)))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/nowPlaying")
    @Override
    public ResponseEntity<GetMoviesResponse> getNowPlayingMovies(
            @RequestParam(value = "page") Integer page,
            @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", format = "Bearer"))
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
            @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", format = "Bearer"))
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
    @GetMapping("/genres")
    @Override
    public ResponseEntity<GetAvailableMovieGenresResponse> getAvailableMovieGenres(
            @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", format = "Bearer"))
            @RequestHeader(name = "Authorization") String accessToken) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getAvailableMovieGenres() ,HttpStatus.OK);
    }

    @Operation(summary = "It returns a list of movies based on their original titles, translated titles, " +
            "alternative titles, or cast member names. " +
            "Allowed params for the sortCriteria: date:desc,rate:desc - date:asc,rate:desc - date:desc,rate:asc - date:asc,rate:asc")
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
            @ApiResponse(responseCode = "200", description = "Movies not found. It returns an empty movie list.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GetMoviesResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping(value = "/search")
    @Override
    public ResponseEntity<GetMoviesResponse> getMoviesByName(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "sortCriteria") String orderBy,
            @Parameter(name = "filters", description = "Ingresar el id del genero a filtrar, separados por coma.", required = true, in = ParameterIn.QUERY,
                    schema = @Schema(type = "string", format = "1,9,46"))
            @RequestParam(value = "filters", required = false) List<String> filters,
            @RequestParam(value = "page") Integer page,
            @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER,
                    schema = @Schema(type = "string", format = "Bearer"))
            @RequestHeader(name = "Authorization") String accessToken) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getMoviesByName(name, orderBy, page, filters), HttpStatus.OK);
    }
}
