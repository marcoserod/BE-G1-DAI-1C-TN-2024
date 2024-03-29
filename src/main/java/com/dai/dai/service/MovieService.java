package com.dai.dai.service;

import com.dai.dai.dto.movie.*;

import java.io.IOException;

public interface MovieService {
    GetMoviesResponse getPopularMovies() throws IOException, InterruptedException;

    GetMoviesResponse getNowPlayingMovies() throws IOException, InterruptedException;

    GetMovieDetailsResponse getMovieById(Integer movieId) throws IOException, InterruptedException;

    GetAvailableMovieGenresResponse getAvailableMovieGenres() throws IOException, InterruptedException;
    
    GetMovieTrailerDetailsResponse getMovieTrailerById(Integer movieId) throws IOException, InterruptedException;

    GetMovieCastResponse getMovieCastByMovieId(Integer movieId) throws IOException, InterruptedException;

}
