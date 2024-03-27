package com.dai.dai.service.impl;


import com.dai.dai.client.movie.impl.MovieDbClientImpl;
import com.dai.dai.dto.movie.GetMovieDetailsResponse;
import com.dai.dai.dto.movie.GetMoviesResponseDto;
import com.dai.dai.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    MovieDbClientImpl movieDbClient;

    public MovieServiceImpl(MovieDbClientImpl movieDbClient) {
        this.movieDbClient = movieDbClient;
    }

    @Override
    public GetMoviesResponseDto getPopularMovies() throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getPopularMovies() .");
        var response = movieDbClient.getPopularMovies();
        log.info("[MovieService] Se recuperan las peliculas correctamente. Cantidad de peliculas: {}.", response.size());
        return GetMoviesResponseDto.builder()
                .movies(response)
                .build();
    }

    @Override
    public GetMoviesResponseDto getNowPlayingMovies() throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getNowPlayingMovies() .");
        var response = movieDbClient.getNowPlaying();
        log.info("[MovieService] Se recuperan las peliculas correctamente. Cantidad de peliculas: {}.", response.size());
        return GetMoviesResponseDto.builder()
                .movies(response)
                .build();
    }

    @Override
    public GetMovieDetailsResponse getMovieById(Integer movieId) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getMovieById(). Id: {}.",movieId);
        var response = movieDbClient.getMovieById(movieId);
        log.info("[MovieService] Se recupera el detalle de la pelicula {} correctamente.", response.getTitle());
        return GetMovieDetailsResponse.builder()
                .movie(response)
                .build();
    }
}
