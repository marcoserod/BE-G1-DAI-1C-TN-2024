package com.dai.dai.service;

import com.dai.dai.dto.movie.GetMovieDetailsResponse;
import com.dai.dai.dto.movie.GetMovieTrailerDetailsResponse;
import com.dai.dai.dto.movie.GetMoviesResponseDto;

import java.io.IOException;

public interface MovieService {
    GetMoviesResponseDto getPopularMovies() throws IOException, InterruptedException;

    GetMoviesResponseDto getNowPlayingMovies() throws IOException, InterruptedException;

    GetMovieDetailsResponse getMovieById(Integer movieId) throws IOException, InterruptedException;

    GetMovieTrailerDetailsResponse getMovieTrailerById(Integer movieId) throws IOException, InterruptedException;
}
