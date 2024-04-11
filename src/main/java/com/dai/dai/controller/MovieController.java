package com.dai.dai.controller;


import com.dai.dai.dto.movie.request.GetMoviesByNameRequest;
import com.dai.dai.dto.movie.response.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface MovieController {

    ResponseEntity<GetMoviesResponse> getPopularMovies() throws IOException, InterruptedException;
    ResponseEntity<GetMoviesResponse> getNowPlayingMovies() throws IOException, InterruptedException;
    ResponseEntity<GetMovieDetailsResponse> getMovieById(Integer movieId) throws IOException, InterruptedException;
    ResponseEntity<GetAvailableMovieGenresResponse> getAvailableMovieGenres() throws IOException, InterruptedException;
    ResponseEntity<GetMovieTrailerDetailsResponse> getMovieTrailerById(Integer movieId) throws IOException, InterruptedException;
    ResponseEntity<GetMovieCastResponse> getMovieCast(Integer movieId) throws IOException, InterruptedException;
    ResponseEntity<GetMoviesResponse> getMoviesByName(GetMoviesByNameRequest name) throws IOException, InterruptedException;
}
