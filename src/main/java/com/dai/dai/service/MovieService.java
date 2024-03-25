package com.dai.dai.service;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.dto.movie.MoviesResponseDto;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    MoviesResponseDto getPopularMovies() throws IOException, InterruptedException;

    MoviesResponseDto getNowPlayingMovies() throws IOException, InterruptedException;
}
