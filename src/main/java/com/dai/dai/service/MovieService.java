package com.dai.dai.service;

import com.dai.dai.client.movie.dto.PostMovieRatingRequest;
import com.dai.dai.client.movie.dto.PostMovieRatingResponse;
import com.dai.dai.dto.movie.response.*;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    GetMoviesResponse getNowPlayingMovies(Integer page, String region) throws IOException, InterruptedException;

    GetMovieDetailsResponse getMovieById(Integer movieId, Long userId) throws IOException, InterruptedException;

    GetAvailableMovieGenresResponse getAvailableMovieGenres() throws IOException, InterruptedException;

    GetMoviesResponse getMoviesByName(String name, String orderBy, Integer page, List<String> filters) throws IOException, InterruptedException;

    PostMovieRatingResponse postMovieRating(Integer movieId, Integer request, Long userId) throws IOException, InterruptedException;

}
