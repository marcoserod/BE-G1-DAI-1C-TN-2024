package com.dai.dai.service.impl;


import com.dai.dai.client.movie.impl.MovieDbClientImpl;
import com.dai.dai.dto.movie.response.*;
import com.dai.dai.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    MovieDbClientImpl movieDbClient;

    public MovieServiceImpl(MovieDbClientImpl movieDbClient) {
        this.movieDbClient = movieDbClient;
    }

    @Override
    public GetMoviesResponse getPopularMovies() throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getPopularMovies() .");
        var response = movieDbClient.getPopularMovies();
        log.info("[MovieService] Se recuperan las peliculas correctamente. Cantidad de peliculas: {}.", response.size());
        return GetMoviesResponse.builder()
                .movies(response)
                .build();
    }

    @Override
    public GetMoviesResponse getNowPlayingMovies() throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getNowPlayingMovies() .");
        var response = movieDbClient.getNowPlaying();
        log.info("[MovieService] Se recuperan las peliculas correctamente. Cantidad de peliculas: {}.", response.size());
        return GetMoviesResponse.builder()
                .movies(response)
                .build();
    }

    @Override
    public GetMovieDetailsResponse getMovieById(Integer movieId) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getMovieById(). Id: {}.",movieId);
        var response = movieDbClient.getMovieById(movieId);
        log.info("[MovieService] Se recupera el detalle de la pelicula {} correctamente.", response.getTitle());
        return GetMovieDetailsResponse.builder()
                .movie(response)
                .build();
    }

    @Override
    public GetAvailableMovieGenresResponse getAvailableMovieGenres() throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getAvailableMovieGenres().");
        var response = movieDbClient.getAvailableMovieGenres();
        log.info("[MovieService] Se recuperan la lista de los generos. Cantidad de generos obtenidos; {}.", response.size());


        return GetAvailableMovieGenresResponse.builder()
                .genreList(response)
                .build();
    }

    @Override
    public GetMovieTrailerDetailsResponse getMovieTrailerById(Integer movieId) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del método getMovieTrailerById(). Id: {}.", movieId);
        var response = movieDbClient.getMovieTrailerById(movieId);
        return GetMovieTrailerDetailsResponse.builder()
                .movieTrailer(response)
                .build();
    }

    @Override
    public GetMovieCastResponse getMovieCastByMovieId(Integer movieId) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del método getMovieCastByMovieId(). Id: {}.", movieId);
        var response = movieDbClient.getMovieCastByMovieId(movieId);
        return GetMovieCastResponse.builder()
                .movieCast(response)
                .build();
    }

    @Override
    public GetMoviesResponse getMoviesByName(String name) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del método getMoviesByName(). name: {}.", name);
        //No permite espacios para hacer la query, por eso se cambia el espacio por un 20.
        String nameAdapted = name.replace(" ", "%20");
        log.info("[MovieService] Nombre adaptado: {}.", name);

        var response = movieDbClient.getMoviesByName(nameAdapted);
        return GetMoviesResponse.builder()
                .movies(response)
                .build();
    }
}
