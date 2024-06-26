package com.dai.dai.client.movie;

import com.dai.dai.client.movie.dto.*;
import com.dai.dai.dto.movie.response.GetMovieByIdResponse;
import com.dai.dai.dto.movie.response.GetMoviesResponse;

import java.io.IOException;
import java.util.List;

public interface MovieDbClient {

    GetMoviesResponse getNowPlaying(Integer page, String region) throws IOException, InterruptedException;
    GetMovieByIdResponse getMovieById(Integer movieId) throws IOException, InterruptedException;
    List<Genre> getAvailableMovieGenres() throws IOException, InterruptedException;
    MovieTrailer getMovieTrailerById(Integer movieId) throws IOException, InterruptedException;
    MovieCast getMovieCastByMovieId(Integer movieId) throws IOException, InterruptedException;
    GetMoviesResponse getMoviesByName(String name)  throws IOException, InterruptedException;
    ImageList getMovieImagesByMovieId(Integer movieId) throws IOException, InterruptedException;
    PostMovieRatingResponse postMovieRating(Integer movieId, PostMovieRatingRequest request) throws IOException, InterruptedException;
}
