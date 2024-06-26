package com.dai.dai.client.movie.impl;

import com.dai.dai.client.movie.MovieDbClient;
import com.dai.dai.client.movie.dto.*;
import com.dai.dai.dto.movie.response.GetMovieByIdResponse;
import com.dai.dai.dto.movie.response.GetMoviesResponse;
import com.dai.dai.dto.movie.response.ListMetadata;
import com.dai.dai.exception.TmdbNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MovieDbClientImpl implements MovieDbClient {

    @Value("${movie.client.Access-Token-Auth}")
    String accesToken;
    Movies movieListApiExt = new Movies();
    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public GetMoviesResponse getNowPlaying(Integer page, String region) throws IOException, InterruptedException {
        log.info("[MovieDbClient] getNowPlaying init");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/now_playing?language=es-MX&page="+page
                        + "&region=" + region))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        List<Movie> movieListReturned;
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        try{
            movieListApiExt = objectMapper.readValue(response.body(), Movies.class);

            movieListReturned = movieListApiExt.getResults().stream()
                    .map(oneMovie -> {
                        Movie movie = new Movie();
                        if (oneMovie.getName() != null){
                            movie.setTitle(oneMovie.getName());
                        }else {
                            movie.setTitle(oneMovie.getTitle());
                        }
                        if (oneMovie.getFirst_air_date() != null){
                            movie.setRelease_date(oneMovie.getFirst_air_date());
                        } else {
                            movie.setRelease_date(oneMovie.getRelease_date());
                        }
                        movie.setId(oneMovie.getId());
                        movie.setPoster_path(oneMovie.getPoster_path());
                        movie.setOverview(oneMovie.getOverview());
                        movie.setVote_average(oneMovie.getVote_average());
                        movie.setVote_count(oneMovie.getVote_count());
                        return movie;
                    })
                    .collect(Collectors.toList());

            var metadata = ListMetadata.builder()
                    .totalRecords(movieListApiExt.getTotal_results())
                    .totalPages(movieListApiExt.getTotal_pages())
                    .currentPage(page)
                    .pageSize(20)
                    .build();
            return GetMoviesResponse.builder()
                    .movies(movieListReturned)
                    .metadata(metadata)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("An error occurred while consulting TMDB Api");
        }
    }

    @Override
    public GetMovieByIdResponse getMovieById(Integer movieId) throws IOException, InterruptedException {
        log.info("[MovieDbClient] getMovieById. Id: {}", movieId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/"+movieId+"?language=es-MX"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        GetMovieByIdResponse movie;
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404){
            log.error("Couldn't find the movie with ID: {}",movieId);
            throw new TmdbNotFoundException("Couldn't find the movie with ID: "+movieId);
        }
        try {
            movie = objectMapper.readValue(response.body(), GetMovieByIdResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while consulting TMDB Api");
        }
        return movie;
    }

    @Override
    public List<Genre> getAvailableMovieGenres() throws IOException, InterruptedException {
        log.info("[MovieDbClient] getAvailableMovieGenres.");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/genre/movie/list?language=es-MX"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        List<Genre> res;
        GenresResponse genresResponse = new GenresResponse();
        try {
            genresResponse = objectMapper.readValue(response.body(), GenresResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while consulting TMDB Api");
        }
        if (!genresResponse.getGenres().isEmpty()){
            return genresResponse.getGenres();
        } else {
            throw new RuntimeException("An error occurred while retrieving the requested genres");
        }
    }
    

    @Override
    public MovieTrailer getMovieTrailerById(Integer movieId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + movieId + "/videos?language=es-MX"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404){
            log.error("No pudimos encontrar el cast asociado a la pelicula de id: {}",movieId);
            throw new TmdbNotFoundException("Unable to retrieve the cast by ID: "+movieId);
        }
        var movieVideo = new MovieTrailer();
        JsonNode resultsNode;
        try {
            var jsonResponse = objectMapper.readTree(response.body());
            resultsNode = jsonResponse.get("results");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while consulting TMDB Api", e);
        }
        if (resultsNode.isArray() && resultsNode.size() > 0) {
            for (JsonNode result : resultsNode) {
                if (result.get("type").asText().equals("Trailer") &&
                        result.get("site").asText().equals("YouTube")) {
                    var link = "https://www.youtube.com/watch?v=" + result.get("key").asText();
                    movieVideo.setLink(link);
                }
            }
        } else {
            throw new TmdbNotFoundException("No trailers were found for the movie with ID: " + movieId);
        }
        return movieVideo;
    }

    @Override
    public MovieCast getMovieCastByMovieId(Integer movieId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/"+movieId+"/credits?language=es-MX"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404){
            log.error("No pudimos encontrar el cast asociado a la pelicula de id: {}",movieId);
            throw new TmdbNotFoundException("We couldn't retrieve the cast by ID: "+movieId);
        }
        MovieCast movieCast;
        try {
            movieCast = objectMapper.readValue(response.body(), MovieCast.class);
            return movieCast;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while consulting TMDB Api");
        }
    }

    @Override
    public GetMoviesResponse getMoviesByName(String name) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/multi?query="+name+"&include_adult=false" +
                        "&language=es-MX&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        GetMoviesResponse movieListReturned = GetMoviesResponse.builder().build();
        ListMetadata listMetadata = ListMetadata.builder().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        try {
            movieListApiExt = objectMapper.readValue(response.body(), Movies.class);
            if(movieListApiExt.getTotal_results().equals(0)){
                log.info("La busqueda no tiene resultados.");
                return GetMoviesResponse.builder()
                        .movies(new ArrayList<>())
                        .metadata(
                                ListMetadata.builder()
                                        .totalPages(0)
                                        .pageSize(0)
                                        .totalRecords(0)
                                        .currentPage(0)
                                        .build())
                        .build();
            }
            if (movieListApiExt.getResults().get(0).getMedia_type().equals("person")) {

                //Todas las peliculas del actor --> actorMovieList
                List<Movie> actorMovieList = new ArrayList<>();
                log.info("Se van a recuperar peliculas asociadas al acrtor: {}.", movieListApiExt.getResults().get(0).getName());
                var actorId = movieListApiExt.getResults().get(0).getId();
                HttpRequest actorRequest = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.themoviedb.org/3/discover/movie?language=es-MX&include_adult=false&" +
                                "page=1&with_cast="+actorId))
                        .header("accept", "application/json")
                        .header("Authorization", "Bearer "+accesToken)
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> actorResponse = HttpClient.newHttpClient().send(actorRequest, HttpResponse.BodyHandlers.ofString());
                var movieListActorRes = objectMapper.readValue(actorResponse.body(), Movies.class);

                //Averiguamos cuantas peliculas tiene el actor en ls bbdd.
                log.info("Cantidad de paginas: {}", movieListActorRes.getTotal_pages());
                log.info("Cantidad de resultados: {}", movieListActorRes.getTotal_results());
                int totalPages = movieListActorRes.getTotal_pages();
                int contadorPaginasTmbd = 1;

                //Consultamos n veces tmdb para conseguir las n paginas que tienen de peliculas.
                while (contadorPaginasTmbd <= totalPages ){
                    actorRequest = HttpRequest.newBuilder()
                            .uri(URI.create("https://api.themoviedb.org/3/discover/movie?language=es-MX&page=" + contadorPaginasTmbd +
                                    "&with_cast="+actorId))
                            .header("accept", "application/json")
                            .header("Authorization", "Bearer "+accesToken)
                            .method("GET", HttpRequest.BodyPublishers.noBody())
                            .build();
                    actorResponse = HttpClient.newHttpClient().send(actorRequest, HttpResponse.BodyHandlers.ofString());
                    movieListActorRes = objectMapper.readValue(actorResponse.body(), Movies.class);
                    var movieList = movieListActorRes.getResults().stream()
                            .map(oneMovie -> {
                                Movie movie = new Movie();
                                if (oneMovie.getName() != null){
                                    movie.setTitle(oneMovie.getName());
                                }else {
                                    movie.setTitle(oneMovie.getTitle());
                                }
                                if (oneMovie.getFirst_air_date() != null){
                                    movie.setRelease_date(oneMovie.getFirst_air_date());
                                } else {
                                    movie.setRelease_date(oneMovie.getRelease_date());
                                }
                                movie.setId(oneMovie.getId());
                                movie.setPoster_path(oneMovie.getPoster_path());
                                movie.setOverview(oneMovie.getOverview());
                                movie.setVote_average(oneMovie.getVote_average());
                                movie.setVote_count(oneMovie.getVote_count());
                                movie.setGenres(oneMovie.getGenres());
                                return movie;
                            })
                            .toList();

                    actorMovieList.addAll(movieList);
                    contadorPaginasTmbd ++;

                }
                log.info("La lista de peliculas del actor tiene: {} resultados.",actorMovieList.size() );
                movieListReturned.setMovies(actorMovieList);
                listMetadata.setPageSize(20);
                listMetadata.setTotalRecords(movieListApiExt.getTotal_results());
                listMetadata.setCurrentPage(1);
                listMetadata.setTotalPages(movieListApiExt.getTotal_pages());
            }
            else{
                List<Movie> movieNameList = new ArrayList<>();
                HttpRequest nameSearch = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.themoviedb.org/3/search/movie?query=" +name+
                                "&include_adult=false&language=es-MX&page=1"))
                        .header("accept", "application/json")
                        .header("Authorization", "Bearer "+accesToken)
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> nameSearchresponse = HttpClient.newHttpClient().send(nameSearch, HttpResponse.BodyHandlers.ofString());
                var searchList = objectMapper.readValue(nameSearchresponse.body(), Movies.class);

                //Averiguamos cuantas peliculas tiene el actor en ls bbdd.
                log.info("Cantidad de paginas: {}", searchList.getTotal_pages());
                log.info("Cantidad de resultados: {}", searchList.getTotal_results());
                int totalPages = movieListApiExt.getTotal_pages();
                int contadorPaginasTmbd = 1;


                //Consultamos n veces tmdb para conseguir las n paginas que tienen de peliculas.
                while (contadorPaginasTmbd <= totalPages ){
                    var searchNameRequest = HttpRequest.newBuilder()
                            .uri(URI.create("https://api.themoviedb.org/3/search/movie?query="+name+"&include_adult=false" +
                                    "&language=es-MX&page="+contadorPaginasTmbd))
                            .header("accept", "application/json")
                            .header("Authorization", "Bearer "+accesToken)
                            .method("GET", HttpRequest.BodyPublishers.noBody())
                            .build();
                    var movieResponse = HttpClient.newHttpClient().send(searchNameRequest, HttpResponse.BodyHandlers.ofString());
                    var movieListRes = objectMapper.readValue(movieResponse.body(), Movies.class);
                    var movieList = movieListRes.getResults().stream()
                            .map(oneMovie -> {
                                Movie movie = new Movie();
                                if (oneMovie.getName() != null){
                                    movie.setTitle(oneMovie.getName());
                                }else {
                                    movie.setTitle(oneMovie.getTitle());
                                }
                                if (oneMovie.getFirst_air_date() != null){
                                    movie.setRelease_date(oneMovie.getFirst_air_date());
                                } else {
                                    movie.setRelease_date(oneMovie.getRelease_date());
                                }
                                movie.setId(oneMovie.getId());
                                movie.setPoster_path(oneMovie.getPoster_path());
                                movie.setOverview(oneMovie.getOverview());
                                movie.setVote_average(oneMovie.getVote_average());
                                movie.setVote_count(oneMovie.getVote_count());
                                movie.setGenres(oneMovie.getGenres());
                                return movie;
                            })
                            .toList();

                    movieNameList.addAll(movieList);
                    contadorPaginasTmbd ++;

                }
                log.info("La lista de peliculas tiene: {} resultados.",movieNameList.size() );
                movieListReturned.setMovies(movieNameList);
                listMetadata.setPageSize(20);
                listMetadata.setTotalRecords(movieListApiExt.getTotal_results());
                listMetadata.setCurrentPage(1);
                listMetadata.setTotalPages(movieListApiExt.getTotal_pages());

            }
            movieListReturned.setMetadata(listMetadata);
            return movieListReturned;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while consulting TMDB Api");
        }
    }

    @Override
    public ImageList getMovieImagesByMovieId(Integer movieId) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/"+ movieId +
                        "/images?include_image_language=es&language=es-MX"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404){
            log.error("No pudimos encontrar las imagenes asociadas a la pelicula de id: {}",movieId);
            throw new TmdbNotFoundException("We couldn't retrieve the  by ID: "+movieId);
        }
        List<Image> imageList = new ArrayList<>();
        JsonNode resultsNode;
        try {
            var jsonResponse = objectMapper.readTree(response.body());
            resultsNode = jsonResponse.get("posters");

            if (resultsNode.isArray() && resultsNode.size() > 0) {
                for (JsonNode result : resultsNode) {
                    var path = result.get("file_path").asText();
                    imageList.add(new Image(path));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while consulting TMDB Api", e);
        }
        return ImageList.builder()
                .images(imageList)
                .build();
    }

    @Override
    public PostMovieRatingResponse postMovieRating(Integer movieId, PostMovieRatingRequest request) throws IOException, InterruptedException {
        HttpRequest postRatingRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/"+movieId+"/rating"))
                .header("accept", "application/json")
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Authorization", "Bearer "+accesToken)
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"value\":"+request.getValue()+"}"))
                .build();
        PostMovieRatingResponse postMovieRatingResponse;
        try{
            HttpResponse<String> response = HttpClient.newHttpClient().send(postRatingRequest, HttpResponse.BodyHandlers.ofString());
            postMovieRatingResponse = objectMapper.readValue(response.body(), PostMovieRatingResponse.class);

            if (postMovieRatingResponse.getSuccess()){
                log.info("Post rating status: "+ postMovieRatingResponse.getStatus_message());
                return postMovieRatingResponse;
            } else {
                log.error("An error occurred when posting rating on TMDB: {}",postMovieRatingResponse.getStatus_message());
                throw new RuntimeException(postMovieRatingResponse.getStatus_message());
            }
        }catch (Exception e){
            log.error("An error occurred when posting rating on TMDB: {}", e.getMessage());
            throw new RuntimeException("An error occurred when posting rating on TMDB.");
        }
    }


}
