package com.dai.dai.service.impl;


import com.dai.dai.client.movie.dto.*;
import com.dai.dai.client.movie.impl.MovieDbClientImpl;
import com.dai.dai.dto.movie.response.*;
import com.dai.dai.entity.UserFavoriteEntity;
import com.dai.dai.entity.UserMovieRatingEntity;
import com.dai.dai.exception.SortCriteriaNotAllowedException;
import com.dai.dai.exception.TmdbNotFoundException;
import com.dai.dai.repository.UserFavoriteRepository;
import com.dai.dai.repository.UserMovieRatingRepository;
import com.dai.dai.service.MovieService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    @Value("${movieplay.page.size}")
    Integer pageSize;
    MovieDbClientImpl movieDbClient;
    UserMovieRatingRepository userMovieRatingRepository;
    UserFavoriteRepository userFavoriteRepository;

    public MovieServiceImpl(MovieDbClientImpl movieDbClient, UserMovieRatingRepository userMovieRatingRepository, UserFavoriteRepository userFavoriteRepository) {
        this.movieDbClient = movieDbClient;
        this.userMovieRatingRepository = userMovieRatingRepository;
        this.userFavoriteRepository = userFavoriteRepository;
    }

    @Override
    public GetMoviesResponse getNowPlayingMovies(Integer page) throws IOException, InterruptedException {
        log.info("[MovieService] Execution of the method getNowPlayingMovies() has started.");
        var response = movieDbClient.getNowPlaying(page);
        log.info("[MovieService] Movies retrieved successfully. Number of movies: {}.", response.getMovies().size());
        return response;
    }

    @Override
    public GetMovieDetailsResponse getMovieById(Integer movieId, Long userId ) {
        log.info("[MovieService] Execution of the method getMovieById() has started. Id: {}.",movieId);
        GetMovieByIdResponse movieDetails = null;
        ImageList movieImages = null;
        MovieCast movieCast = null;
        MovieTrailer movieTrailer = null;
        Integer userRating = null;
        Boolean isUserFvorite;
        List<Genre> genreList;
        try{
            movieDetails = movieDbClient.getMovieById(movieId);
        } catch (Exception e) {
            log.error("The movie with id: {} was not found.", movieId);
            throw new TmdbNotFoundException("No details were found for the requested movie.");
        }
        genreList = movieDetails.getGenres();
        movieDetails.setGenres(null);
        try{
            movieImages = movieDbClient.getMovieImagesByMovieId(movieId);
        } catch (Exception e) {
            log.error("No images were found for the movie with id: {}", movieId);
        }
        try{
            movieCast = movieDbClient.getMovieCastByMovieId(movieId);
        } catch (Exception e) {
            log.error("No cast found for the movie with ID: {}", movieId);
        }
        try{
            movieTrailer = movieDbClient.getMovieTrailerById(movieId);
        } catch (Exception e) {
            log.error("No trailer found for the movie with ID: {}", movieId);
        }
        try {
            userRating = getUserMovieRating(userId, movieId.longValue()).getRating();
        } catch (TmdbNotFoundException exception) {
            log.info("Movie has no ratings by user.");
        } catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        isUserFvorite = isUserFavorite(userId.intValue(), movieId);
        log.info("[MovieService] Movie details for {} retrieved successfully.", movieDetails.getTitle());

        return GetMovieDetailsResponse.builder()
                .movie(movieDetails)
                .movieCast(movieCast)
                .movieTrailer(movieTrailer)
                .imageList(movieImages)
                .genreList(genreList)
                .userRating(userRating)
                .isUserFavorite(isUserFvorite)
                .build();
    }

    @Override
    public GetAvailableMovieGenresResponse getAvailableMovieGenres() throws IOException, InterruptedException {
        log.info("[MovieService] Execution of the method getAvailableMovieGenres() has started.");
        var response = movieDbClient.getAvailableMovieGenres();
        log.info("[MovieService] The list of genres has been retrieved. Number of genres obtained: {}.", response.size());


        return GetAvailableMovieGenresResponse.builder()
                .genreList(response)
                .build();
    }

    @Override
    public GetMoviesResponse getMoviesByName(String name, String orderBy, Integer page, List<String> filters) throws IOException, InterruptedException {
        log.info("[MovieService] Execution of the method getMoviesByName() has started. name: {}.", name);

        //No permite espacios para hacer la query, por eso se cambia el espacio por un 20.
        String nameAdapted = name.replace(" ", "%20");
        log.info("[MovieService] Adapted name: {}.", name);

        //Recupera todas las peliculas que encuentre.
        var response = movieDbClient.getMoviesByName(nameAdapted);
        if (response.getMetadata().getTotalRecords().equals(0)) {
            log.info("Movies not found with name: {}",name);
            return response;
        }
        removeEmptyOrNullMovies(response.getMovies());
        List<Movie> movieFilteredList;
        if (filters == null || filters.isEmpty()){
            movieFilteredList = response.getMovies();
        } else {
            movieFilteredList = filterGenres(filters, response.getMovies());
        }
        int totalPages = (int) Math.ceil((float) movieFilteredList.size() / pageSize);
        int totalRecords = movieFilteredList.size();
        List<Movie> sortedResponse;
        switch (orderBy){
            case "date:desc,rate:desc":
                log.info("Searches will be sorted according to the following criterion: " +
                        "Descending dates and descending ratings.");
                sortMoviesByReleaseDateDescendingAndRatingDescending(movieFilteredList);
                sortedResponse = paginate(page, movieFilteredList, totalPages);
                return GetMoviesResponse.builder()
                        .movies(sortedResponse)
                        .metadata(setMetadata(sortedResponse, page, totalPages, totalRecords))
                        .build();
            case "date:asc,rate:desc":
                log.info("Searches will be sorted according to the following criterion: " +
                        "Ascending dates and descending ratings.");
                sortMoviesByReleaseDateAscendingAndRatingDescending(movieFilteredList);
                sortedResponse = paginate(page, movieFilteredList,totalPages);
                return GetMoviesResponse.builder()
                        .movies(sortedResponse)
                        .metadata(setMetadata(sortedResponse, page, totalPages, totalRecords))
                        .build();
            case "date:desc,rate:asc":
                log.info("Searches will be sorted according to the following criterion: " +
                        "Descending dates and ascending ratings.");
                sortMoviesByReleaseDateDescendingAndRatingAscending(movieFilteredList);
                sortedResponse = paginate(page, movieFilteredList, totalPages);
                return GetMoviesResponse.builder()
                        .movies(sortedResponse)
                        .metadata(setMetadata(sortedResponse, page, totalPages, totalRecords))
                        .build();
            case "date:asc,rate:asc":
                log.info("Searches will be sorted according to the following criterion: " +
                        "Descending dates and ascending ratings.");
                sortMoviesByReleaseDateAscendingAndRatingAscending(movieFilteredList);
                sortedResponse = paginate(page, movieFilteredList, totalPages);
                return GetMoviesResponse.builder()
                        .movies(sortedResponse)
                        .metadata(setMetadata(sortedResponse, page, totalPages, totalRecords))
                        .build();
            default:
                log.error("Unknown sorting criterion.");
                throw new SortCriteriaNotAllowedException("Unknown sorting criterion.");
        }

    }

    @Override
    public PostMovieRatingResponse postMovieRating(Integer movieId, Integer rating, Long userId) throws IOException, InterruptedException {
        log.info("[MovieService] Execution of the method postMovieRating() has started.");
        float tmdbRating = (rating * 2);
        PostMovieRatingRequest postMovieRatingRequest = PostMovieRatingRequest.builder()
                .value(tmdbRating)
                .build();
        var response = movieDbClient.postMovieRating(movieId,postMovieRatingRequest);

        var userMovieRating = userMovieRatingRepository.getUserMovieRatingEntity(userId, movieId.longValue());
        if (userMovieRating.isEmpty()){
            UserMovieRatingEntity userMovieRatingEntity = new UserMovieRatingEntity();
            userMovieRatingEntity.setMovie_id(movieId.longValue());
            userMovieRatingEntity.setUser_id(userId);
            userMovieRatingEntity.setRating(rating);
            saveUserMovieRating(userMovieRatingEntity);
        } else {
            log.info("Updating movie rating...");
            userMovieRating.get().setRating(rating);
            log.info("New Movie Rating: {}", userMovieRating.get().getRating());
            saveUserMovieRating(userMovieRating.get());
        }


        return response;
    }


    private void removeEmptyOrNullMovies(List<Movie> list) {
        for (int i = list.size()-1 ; i>=0 ; i-- ){
            if (list.get(i).getRelease_date() == null || list.get(i).getRelease_date().isEmpty() ||
                    list.get(i).getOverview() == null || list.get(i).getOverview().isEmpty() ||
                    list.get(i).getVote_count() == null || list.get(i).getVote_count().equals(0L) ||
                    list.get(i).getPoster_path() == null || list.get(i).getPoster_path().isEmpty()){
                log.info("The movie with id: {} was deleted.", list.get(i).getId());
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy"); // Ajusta el formato según sea necesario
        try {
            return format.parse(releaseDateStr);
        } catch (ParseException e) {
            // Maneja la excepción de análisis (por ejemplo, registra el error, devuelve null)
            throw new RuntimeException("Error parsing release date: " + releaseDateStr, e);
        }
    }

    private ListMetadata setMetadata( List<Movie> movies, Integer page, Integer totalPages, Integer totalRecords){
        return ListMetadata.builder()
                .currentPage(page)
                .pageSize(movies.size())
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .build();
    }


    private List<Movie> paginate(Integer page, List<Movie> movies, Integer totalPages){
        log.info("Page {} of movies generated.", page);
        int firstItem = page * pageSize - (pageSize);
        int lastItem;
        if (totalPages.equals(page) || totalPages.equals(0)){
            lastItem = movies.size();
        }else {
            lastItem = page * pageSize ;
        }
        return movies.subList(firstItem,lastItem);


    }

    private List<Movie> filterGenres(List<String> filters, List<Movie> movies ){

        return movies.stream()
                .filter(movie -> movie.getGenres().stream()
                        .anyMatch(genre -> filters.contains(genre.toString())))
                .collect(Collectors.toList());
    }

    private void saveUserMovieRating(UserMovieRatingEntity userMovieRatingEntity){
        try {
            userMovieRatingRepository.save(userMovieRatingEntity);
        } catch (Exception e) {
            log.error("Ocurrió un error al persistir el rating en la bbdd. Error: {}", e.getMessage());
            throw new RuntimeException("Ocurrió un error al persistir el rating en la bbdd.");
        }
    }

    private UserMovieRatingEntity getUserMovieRating(Long userId, Long filmId){
        Optional<UserMovieRatingEntity> response;
        try {
            response = userMovieRatingRepository.getUserMovieRatingEntity(userId,filmId);
        } catch (Exception e){
            log.error("Ocurrió un error al recuperar el rating de la base de datos.");
            throw new RuntimeException("Ocurrió un error al recuperar el rating de la base de datos.");
        }
        if (response.isPresent()){
            return response.get();
        } else {
            throw new TmdbNotFoundException("No se encontró rating para la pelicula.");
        }
    }

        private Boolean isUserFavorite(Integer userId, Integer filmId){

        UserFavoriteEntity response;
        try {
            response = userFavoriteRepository.findByUserIdAndFilmId(userId, filmId);
        } catch (Exception e){
            log.error("Ocurrió un error al recuperar el rating de la base de datos.");
            throw new RuntimeException("Ocurrió un error al recuperar el rating de la base de datos.");
        }
        if (null != response){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
