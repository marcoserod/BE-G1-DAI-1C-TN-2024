package com.dai.dai.client.movie;

import com.dai.dai.client.movie.dto.Genre;
import com.dai.dai.client.movie.dto.Movie;

import java.io.IOException;
import java.util.List;

public interface MovieDbClient {

    List<Movie> getPopularMovies() throws IOException, InterruptedException;
    List<Movie> getNowPlaying() throws IOException, InterruptedException;
    Movie getMovieById(Integer movieId) throws IOException, InterruptedException;
    List<Genre> getAvailableMovieGenres() throws IOException, InterruptedException;
}
