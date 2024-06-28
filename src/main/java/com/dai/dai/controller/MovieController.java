package com.dai.dai.controller;


import com.dai.dai.client.movie.dto.PostMovieRatingResponse;
import com.dai.dai.dto.movie.request.RateMovieRequest;
import com.dai.dai.dto.movie.response.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface MovieController {
    ResponseEntity<GetMoviesResponse> getNowPlayingMovies(Integer page, String region, String accessToken)
            throws IOException, InterruptedException;
    ResponseEntity<GetMovieDetailsResponse> getMovieById(Integer movieId, String accessToken)
            throws IOException, InterruptedException;
    ResponseEntity<GetAvailableMovieGenresResponse> getAvailableMovieGenres(String accessToken)
            throws IOException, InterruptedException;
    ResponseEntity<GetMoviesResponse> getMoviesByName(String name, String orderBy,
                                                      List<String> filters,
                                                      Integer page,
                                                      String accessToken)
            throws IOException, InterruptedException;

    ResponseEntity<PostMovieRatingResponse> postMovieRating(Integer movieId, RateMovieRequest request, String accessToken) throws IOException, InterruptedException;

}
