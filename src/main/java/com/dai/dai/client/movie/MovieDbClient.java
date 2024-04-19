package com.dai.dai.client.movie;

import com.dai.dai.client.movie.dto.*;

import java.io.IOException;
import java.util.List;

public interface MovieDbClient {

    List<Movie> getNowPlaying(Integer page) throws IOException, InterruptedException;
    Movie getMovieById(Integer movieId) throws IOException, InterruptedException;
    List<Genre> getAvailableMovieGenres() throws IOException, InterruptedException;
    MovieTrailer getMovieTrailerById(Integer movieId) throws IOException, InterruptedException;
    MovieCast getMovieCastByMovieId(Integer movieId) throws IOException, InterruptedException;
    List<Movie>  getMoviesByName(String name)  throws IOException, InterruptedException;
    ImageList getMovieImagesByMovieId(Integer movieId) throws IOException, InterruptedException;
}
