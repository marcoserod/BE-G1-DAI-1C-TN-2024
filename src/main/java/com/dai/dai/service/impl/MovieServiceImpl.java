package com.dai.dai.service.impl;


import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.client.movie.impl.MovieDbClientImpl;
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
    public List<Movie> movieAuth() throws IOException, InterruptedException {
        log.info("Comienza la ejecución del metodo Auth.");
        var response = movieDbClient.getPopularMovies();
        return response;
    }
}
