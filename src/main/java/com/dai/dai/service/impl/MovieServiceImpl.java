package com.dai.dai.service.impl;


import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.client.movie.impl.MovieDbClientImpl;
import com.dai.dai.dto.movie.MoviesResponseDto;
import com.dai.dai.exception.DaiException;
import com.dai.dai.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    MovieDbClientImpl movieDbClient;

    public MovieServiceImpl(MovieDbClientImpl movieDbClient) {
        this.movieDbClient = movieDbClient;
    }

    @Override
    public MoviesResponseDto getPopularMovies() throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getPopularMovies() .");
        var response = movieDbClient.getPopularMovies();
        log.info("[MovieService] Se recuperan las peliculas correctamente. Cantidad de peliculas: {}.", response.size());
        return MoviesResponseDto.builder()
                .movies(response)
                .build();
    }

    @Override
    public MoviesResponseDto getNowPlayingMovies() throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getNowPlayingMovies() .");
        var response = movieDbClient.getNowPlaying();
        log.info("[MovieService] Se recuperan las peliculas correctamente. Cantidad de peliculas: {}.", response.size());
        return MoviesResponseDto.builder()
                .movies(response)
                .build();
    }
}
