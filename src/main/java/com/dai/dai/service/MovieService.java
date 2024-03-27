package com.dai.dai.service;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.dto.movie.GetMovieDetailsResponse;
import com.dai.dai.dto.movie.GetMoviesResponseDto;

import java.io.IOException;

public interface MovieService {
    GetMoviesResponseDto getPopularMovies() throws IOException, InterruptedException;

    GetMoviesResponseDto getNowPlayingMovies() throws IOException, InterruptedException;

    GetMovieDetailsResponse getMovieById(Integer movieId) throws IOException, InterruptedException;
}
