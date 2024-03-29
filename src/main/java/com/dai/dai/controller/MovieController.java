package com.dai.dai.controller;


import com.dai.dai.dto.movie.GetAvailableMovieGenresResponse;
import com.dai.dai.dto.movie.GetMovieDetailsResponse;
import com.dai.dai.dto.movie.GetMoviesResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface MovieController {

    ResponseEntity<GetMoviesResponseDto> getPopularMovies() throws IOException, InterruptedException;
    ResponseEntity<GetMoviesResponseDto> getNowPlayingMovies() throws IOException, InterruptedException;
    ResponseEntity<GetMovieDetailsResponse> getMovieById(Integer movieId) throws IOException, InterruptedException;
    ResponseEntity<GetAvailableMovieGenresResponse> getAvailableMovieGenres() throws IOException, InterruptedException;

}
