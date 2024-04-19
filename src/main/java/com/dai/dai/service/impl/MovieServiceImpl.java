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
    public GetMoviesResponse getNowPlayingMovies(Integer page) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getNowPlayingMovies() .");
        var response = movieDbClient.getNowPlaying(page);
        log.info("[MovieService] Se recuperan las peliculas correctamente. Cantidad de peliculas: {}.", response.size());
        return GetMoviesResponse.builder()
                .movies(response)
                .build();
    }

    @Override
    public GetMovieDetailsResponse getMovieById(Integer movieId) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getMovieById(). Id: {}.",movieId);
        var movieImages = movieDbClient.getMovieImagesByMovieId(movieId);
        var movieDetails = movieDbClient.getMovieById(movieId);
        var movieCast = movieDbClient.getMovieCastByMovieId(movieId);
        var movieTrailer = movieDbClient.getMovieTrailerById(movieId);
        log.info("[MovieService] Se recupera el detalle de la pelicula {} correctamente.", movieDetails.getTitle());
        return GetMovieDetailsResponse.builder()
                .movie(movieDetails)
                .movieCast(movieCast)
                .movieTrailer(movieTrailer)
                .imageList(movieImages)
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
