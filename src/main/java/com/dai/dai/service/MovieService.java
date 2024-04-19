package com.dai.dai.service;

import com.dai.dai.dto.movie.response.*;

import java.io.IOException;

public interface MovieService {

    GetMoviesResponse getNowPlayingMovies(Integer page) throws IOException, InterruptedException;

    GetMovieDetailsResponse getMovieById(Integer movieId) throws IOException, InterruptedException;

    GetAvailableMovieGenresResponse getAvailableMovieGenres() throws IOException, InterruptedException;

    GetMoviesResponse getMoviesByName(String name) throws IOException, InterruptedException;

}
