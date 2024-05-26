package com.dai.dai.service.impl;


import com.cloudinary.api.exceptions.NotFound;
import com.dai.dai.client.movie.dto.ImageList;
import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.client.movie.dto.MovieCast;
import com.dai.dai.client.movie.dto.MovieTrailer;
import com.dai.dai.client.movie.impl.MovieDbClientImpl;
import com.dai.dai.dto.movie.response.*;
import com.dai.dai.exception.SortCriteriaNotAllowedException;
import com.dai.dai.exception.TmdbNotFoundException;
import com.dai.dai.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    MovieDbClientImpl movieDbClient;
    @Value("${movieplay.page.size}")
    Integer pageSize;

    public MovieServiceImpl(MovieDbClientImpl movieDbClient) {
        this.movieDbClient = movieDbClient;
    }



    @Override
    public GetMoviesResponse getNowPlayingMovies(Integer page) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getNowPlayingMovies() .");
        var response = movieDbClient.getNowPlaying(page);
        log.info("[MovieService] Se recuperan las peliculas correctamente. Cantidad de peliculas: {}.", response.getMovies().size());
        return response;
    }

    @Override
    public GetMovieDetailsResponse getMovieById(Integer movieId) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del metodo getMovieById(). Id: {}.",movieId);
        Movie movieDetails = null;
        ImageList movieImages = null;
        MovieCast movieCast = null;
        MovieTrailer movieTrailer = null;
        try{
            movieDetails = movieDbClient.getMovieById(movieId);
        } catch (Exception e) {
            log.error("No se encontró la pelicula id: {}", movieId);
            throw new TmdbNotFoundException("No se encontraron detalles para la pelicula solicitada.");
        }
        try{
            movieImages = movieDbClient.getMovieImagesByMovieId(movieId);
        } catch (Exception e) {
            log.error("No se encontraron imagenes para la pelicula de id: {}", movieId);
        }
        try{
            movieCast = movieDbClient.getMovieCastByMovieId(movieId);
        } catch (Exception e) {
            log.error("No se encontró el  para la pelicula de id: {}", movieId);
        }
        try{
            movieTrailer = movieDbClient.getMovieTrailerById(movieId);
        } catch (Exception e) {
            log.error("No se encontró el  para la pelicula de id: {}", movieId);
        }
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
    public GetMoviesResponse getMoviesByName(String name, String orderBy, Integer page) throws IOException, InterruptedException {
        log.info("[MovieService] Comienza la ejecución del método getMoviesByName(). name: {}.", name);

        //No permite espacios para hacer la query, por eso se cambia el espacio por un 20.
        String nameAdapted = name.replace(" ", "%20");
        log.info("[MovieService] Nombre adaptado: {}.", name);

        //Recupera todas las peliculas que encuentre.
        var response = movieDbClient.getMoviesByName(nameAdapted);
        removeEmptyOrNullMovies(response.getMovies());
        int totalPages = (int) Math.ceil((float) response.getMovies().size() / pageSize);
        List<Movie> sortedResponse;
        switch (orderBy){
            case "date:desc,rate:desc":
                log.info("Las busquedas se van a organizar con el siguiente criterio: " +
                        "Fechas descendente y Rating descendente.");
                sortMoviesByReleaseDateDescendingAndRatingDescending(response.getMovies());
                sortedResponse = paginate(page, response.getMovies(), totalPages);
                return GetMoviesResponse.builder()
                        .movies(sortedResponse)
                        .metadata(setMetadata(sortedResponse, page, totalPages))
                        .build();
            case "date:asc,rate:desc":
                log.info("Las busquedas se van a organizar con el siguiente criterio: " +
                        "Fechas Ascendente y Rating descendente.");
                sortMoviesByReleaseDateAscendingAndRatingDescending(response.getMovies());
                sortedResponse = paginate(page, response.getMovies(),totalPages);
                return GetMoviesResponse.builder()
                        .movies(sortedResponse)
                        .metadata(setMetadata(sortedResponse, page, totalPages))
                        .build();
            case "date:desc,rate:asc":
                log.info("Las busquedas se van a organizar con el siguiente criterio: " +
                        "Fechas descendente y Rating Ascendente.");
                sortMoviesByReleaseDateDescendingAndRatingAscending(response.getMovies());
                sortedResponse = paginate(page, response.getMovies(), totalPages);
                return GetMoviesResponse.builder()
                        .movies(sortedResponse)
                        .metadata(setMetadata(sortedResponse, page, totalPages))
                        .build();
            case "date:asc,rate:asc":
                log.info("Las busquedas se van a organizar con el siguiente criterio: " +
                        "Fechas descendente y Rating Ascendente.");
                sortMoviesByReleaseDateAscendingAndRatingAscending(response.getMovies());
                sortedResponse = paginate(page, response.getMovies(), totalPages);
                return GetMoviesResponse.builder()
                        .movies(sortedResponse)
                        .metadata(setMetadata(sortedResponse, page, totalPages))
                        .build();
            default:
                log.error("Criterio de ordenamiento desconocido.");
                throw new SortCriteriaNotAllowedException("Criterio de ordenamiento desconocido.");
        }

    }


    private void removeEmptyOrNullMovies(List<Movie> list) {
        for (int i = list.size()-1 ; i>=0 ; i-- ){
            if (list.get(i).getRelease_date() == null || list.get(i).getRelease_date().isEmpty() ||
                    list.get(i).getOverview().isEmpty() || list.get(i).getVote_count().equals(0L) || list.get(i).getPoster_path().isEmpty()){
                log.info("Se eliminó la pelicula de id: {}", list.get(i).getId());
                list.remove(i);
            }
        }
    }

    private void sortMoviesByReleaseDateAscendingAndRatingAscending(List<Movie> list) {
        list.sort(new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                // Compare by release date first
                Date date1 = parseReleaseDate(movie1.getRelease_date());
                Date date2 = parseReleaseDate(movie2.getRelease_date());
                int releaseDateComparison = date1.compareTo(date2);

                // If release dates are equal, compare by rating
                if (releaseDateComparison == 0) {
                    Double rating1 = movie1.getVote_average(); // Assuming rating is a Double
                    Double rating2 = movie2.getVote_average();
                    return rating1.compareTo(rating2); // Ascending order for rating
                }

                // Otherwise, return the release date comparison result
                return releaseDateComparison;
            }
        });
    }

    private void sortMoviesByReleaseDateDescendingAndRatingDescending(List<Movie> list) {
        list.sort(new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                Date date1 = parseReleaseDate(movie1.getRelease_date());
                Date date2 = parseReleaseDate(movie2.getRelease_date());
                int releaseDateComparison = date2.compareTo(date1); // Reverse order for descending

                if (releaseDateComparison == 0) {
                    Double rating1 = movie1.getVote_average();
                    Double rating2 = movie2.getVote_average();
                    return rating2.compareTo(rating1); // Reverse order for descending
                }

                return releaseDateComparison;
            }
        });
    }

    private void sortMoviesByReleaseDateAscendingAndRatingDescending(List<Movie> list) {
        list.sort(new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                Date date1 = parseReleaseDate(movie1.getRelease_date());
                Date date2 = parseReleaseDate(movie2.getRelease_date());
                int releaseDateComparison = date1.compareTo(date2);

                if (releaseDateComparison == 0) {
                    Double rating1 = movie1.getVote_average();
                    Double rating2 = movie2.getVote_average();
                    return rating2.compareTo(rating1); // Reverse order for descending
                }

                return releaseDateComparison;
            }
        });
    }

    private void sortMoviesByReleaseDateDescendingAndRatingAscending(List<Movie> list) {
        list.sort(new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                Date date1 = parseReleaseDate(movie1.getRelease_date());
                Date date2 = parseReleaseDate(movie2.getRelease_date());
                int releaseDateComparison = date2.compareTo(date1); // Reverse order for descending

                if (releaseDateComparison == 0) {
                    Double rating1 = movie1.getVote_average();
                    Double rating2 = movie2.getVote_average();
                    return rating1.compareTo(rating2);
                }

                return releaseDateComparison;
            }
        });
    }


    private static Date parseReleaseDate(String releaseDateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // Ajusta el formato según sea necesario
        try {
            return format.parse(releaseDateStr);
        } catch (ParseException e) {
            // Maneja la excepción de análisis (por ejemplo, registra el error, devuelve null)
            throw new RuntimeException("Error al analizar la fecha de lanzamiento: " + releaseDateStr, e);
        }
    }

    private ListMetadata setMetadata( List<Movie> movies, Integer page, Integer totalPages){
        return ListMetadata.builder()
                .currentPage(page)
                .pageSize(movies.size())
                .totalPages(totalPages)
                .build();
    }


    private List<Movie> paginate(Integer page, List<Movie> movies, Integer totalPages){
        log.info("Se generar la pagina {} de las peliculas.", page);
        int firstItem = page * pageSize - (pageSize);
        int lastItem;
        if (totalPages.equals(page)){
            lastItem = movies.size();
        }else {
            lastItem = page * pageSize ;
        }
        return movies.subList(firstItem,lastItem);


    }
}
