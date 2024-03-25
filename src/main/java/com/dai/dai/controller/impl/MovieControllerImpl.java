package com.dai.dai.controller.impl;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.controller.MovieController;
import com.dai.dai.dto.movie.MoviesResponseDto;
import com.dai.dai.exception.DaiException;
import com.dai.dai.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<MoviesResponseDto> getPopularMovies() throws IOException, InterruptedException {
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
    public ResponseEntity<MoviesResponseDto> getNowPlayingMovies() throws IOException, InterruptedException {
        return new ResponseEntity<>(movieService.getNowPlayingMovies(), HttpStatus.OK);
    }
}
