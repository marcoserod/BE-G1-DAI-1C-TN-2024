package com.dai.dai.controller;


import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.dto.movie.MoviesResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public interface MovieController {

    ResponseEntity<MoviesResponseDto> getPopularMovies() throws IOException, InterruptedException;
    ResponseEntity<MoviesResponseDto> getNowPlayingMovies() throws IOException, InterruptedException;
}
