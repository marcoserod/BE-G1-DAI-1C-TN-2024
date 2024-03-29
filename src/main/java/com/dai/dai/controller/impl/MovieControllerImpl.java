package com.dai.dai.controller.impl;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.controller.MovieController;
import com.dai.dai.dto.movie.*;
import com.dai.dai.exception.DaiException;
import com.dai.dai.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@AllArgsConstructor
@RestController
@Tag(name = "Movie Controller", description = "Endpoints para operaciones relacionadas con TMDB Api.")
@RequestMapping("/movies")
public class MovieControllerImpl implements MovieController {

    MovieService movieService;


    @Operation(summary = "Retorna una lista de películas populares, permitiendo a los usuarios acceder fácilmente a" +
            " contenido de tendencia.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Movie.class)))}),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor.",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/popular")
    @Override
    public ResponseEntity<GetMoviesResponse> getPopularMovies() throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getPopularMovies(), HttpStatus.OK);
    }

    @Operation(summary = "Retorna una lista de Peliculas que estan actualmente en los cines.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Movie.class)))}),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor.",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/now_playing")
    @Override
    public ResponseEntity<GetMoviesResponse> getNowPlayingMovies() throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getNowPlayingMovies(), HttpStatus.OK);
    }

    @Operation(summary = "Retorna los detalles de la pelicula solicitada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GetMovieDetailsResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "No se encontró la pelicula solicitada.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor.",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/details/{movie_id}")
    @Override
    public ResponseEntity<GetMovieDetailsResponse> getMovieById(@Valid @PathVariable(value = "movie_id" ) Integer movieId) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getMovieById(movieId), HttpStatus.OK);
    }

    @Operation(summary = "Retorna los generos de pelicula disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GetAvailableMovieGenresResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor.",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/genre/list")
    @Override
    public ResponseEntity<GetAvailableMovieGenresResponse> getAvailableMovieGenres() throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getAvailableMovieGenres() ,HttpStatus.OK);
    }

    @Operation(summary = "Retorna el trailer de la película solicitada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GetMovieTrailerDetailsResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "No se encontró el Trailer solicitado.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor.",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/trailer/{movie_id}")
    @Override
    public ResponseEntity<GetMovieTrailerDetailsResponse> getMovieTrailerById
    (@Valid @PathVariable(value = "movie_id") Integer movieId) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getMovieTrailerById(movieId), HttpStatus.OK);
    }

    @Operation(summary = "Retorna el reparto y los directores de la película solicitada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GetMovieTrailerDetailsResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "No se encontró el Cast solicitado.",
                    content = { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class)) }),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor.",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DaiException.class))) })
    @GetMapping("/cast/{movie_id}")
    @Override
    public ResponseEntity<GetMovieCastResponse> getMovieCast(@Valid @PathVariable(value = "movie_id")Integer movieId) throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getMovieCastByMovieId(movieId), HttpStatus.OK);
    }
}
