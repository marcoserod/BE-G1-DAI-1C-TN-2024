package com.dai.dai.client.movie;

import com.dai.dai.client.movie.dto.Genre;
import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.client.movie.dto.MovieCast;
import com.dai.dai.client.movie.dto.MovieTrailer;

import java.io.IOException;
import java.util.List;

public interface MovieDbClient {

    List<Movie> getPopularMovies() throws IOException, InterruptedException;
    List<Movie> getNowPlaying() throws IOException, InterruptedException;
    Movie getMovieById(Integer movieId) throws IOException, InterruptedException;
    List<Genre> getAvailableMovieGenres() throws IOException, InterruptedException;
    MovieTrailer getMovieTrailerById(Integer movieId) throws IOException, InterruptedException;
    MovieCast getMovieCastByMovieId(Integer movieId) throws IOException, InterruptedException;
    List<Movie>  getMoviesByName(String name)  throws IOException, InterruptedException;

}
