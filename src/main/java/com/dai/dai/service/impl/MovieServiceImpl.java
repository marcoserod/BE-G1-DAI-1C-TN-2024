package com.dai.dai.service.impl;


import com.dai.dai.client.movie.impl.MovieDbClientImpl;
import com.dai.dai.dto.movie.response.*;
import com.dai.dai.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;


@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    MovieDbClientImpl movieDbClient;
    @Value("${movie.service.filter.case1}")
    private String CASE1;
    @Value("${movie.service.filter.case2}")
    private String CASE2;
    @Value("${movie.service.filter.case3}")
    private String CASE3;
    @Value("${movie.service.filter.case4}")
    private String CASE4;

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
    public GetMoviesResponse getMoviesByName(String name, String orderBy) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del método getMoviesByName(). name: {}.", name);

        if (orderBy != null){
            //TODO Switch con diferentes filtros.


        } else {
            log.error("Criterio de ordenamiento desconocido.");
            throw new BadRequestException();
        }

        //No permite espacios para hacer la query, por eso se cambia el espacio por un 20.
        String nameAdapted = name.replace(" ", "%20");
        log.info("[MovieService] Nombre adaptado: {}.", name);

        var response = movieDbClient.getMoviesByName(nameAdapted);
        return GetMoviesResponse.builder()
                .movies(response)
                .build();
    }
}
