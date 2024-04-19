package com.dai.dai.controller;


import com.dai.dai.dto.movie.response.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface MovieController {

    ResponseEntity<GetMoviesResponse> getPopularMovies(Integer page, String accessToken) throws IOException, InterruptedException;
    ResponseEntity<GetMoviesResponse> getNowPlayingMovies(Integer page, String accessToken) throws IOException, InterruptedException;
    ResponseEntity<GetMovieDetailsResponse> getMovieById(Integer movieId, String accessToken) throws IOException, InterruptedException;
    ResponseEntity<GetAvailableMovieGenresResponse> getAvailableMovieGenres(String accessToken) throws IOException, InterruptedException;
    ResponseEntity<GetMoviesResponse> getMoviesByName(String name, Integer page, String accessToken) throws IOException, InterruptedException;
}
