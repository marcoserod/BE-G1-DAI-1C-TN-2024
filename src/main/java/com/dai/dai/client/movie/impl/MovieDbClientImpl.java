package com.dai.dai.client.movie.impl;

import com.dai.dai.client.movie.MovieDbClient;
import com.dai.dai.client.movie.dto.*;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class MovieDbClientImpl implements MovieDbClient {

    @Value("${movie.client.Access-Token-Auth}")
    String accesToken;
    Movies movieListApiExt = new Movies();
    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public List<Movie> getNowPlaying(Integer page) throws IOException, InterruptedException {
        log.info("[MovieDbClient] getNowPlaying init");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/now_playing?language=es&page="+page))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return this.getMovieListRequest(request);
    }

    @Override
    public Movie getMovieById(Integer movieId) throws IOException, InterruptedException {
        log.info("[MovieDbClient] getMovieById. Id: {}", movieId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/"+movieId))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        Movie movie = new Movie();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404){
            log.error("Couldn't find the movie with ID: {}",movieId);
            throw new TmdbNotFoundException("Couldn't find the movie with ID: "+movieId);
        }
        try {
            movie = objectMapper.readValue(response.body(), Movie.class);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while consulting TMDB Api");
        }
        return movie;
    }

    @Override
    public List<Genre> getAvailableMovieGenres() throws IOException, InterruptedException {
        log.info("[MovieDbClient] getAvailableMovieGenres.");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/genre/movie/list?language=es"))
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

    private List<Movie> getMovieListRequest(HttpRequest request) throws IOException, InterruptedException {
        List<Movie> movieListReturned;
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        try {
           movieListApiExt = objectMapper.readValue(response.body(), Movies.class);
           movieListReturned = movieListApiExt.getResults().stream()
                    .map(oneMovie -> {
                        Movie movie = new Movie();
                        movie.setId(oneMovie.getId());
                        movie.setTitle(oneMovie.getTitle());
                        movie.setPoster_path(oneMovie.getPoster_path());
                        movie.setOverview(oneMovie.getOverview());
                        movie.setRelease_date(oneMovie.getRelease_date());
                        movie.setVote_average(oneMovie.getVote_average());
                        movie.setVote_count(oneMovie.getVote_count());
                        return movie;
                    })
                    .collect(Collectors.toList());
            return movieListReturned;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while consulting TMDB Api");
        }

    }

    @Override
    public MovieTrailer getMovieTrailerById(Integer movieId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + movieId + "/videos"))
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
                .uri(URI.create("https://api.themoviedb.org/3/movie/"+movieId+"/credits?language=es"))
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
    public List<Movie> getMoviesByName(String name) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/multi?query="+name+"&include_adult=true" +
                        "&language=es&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        List<Movie> res =  this.getMovieListRequest(request);

        return res;
    }

    @Override
    public ImageList getMovieImagesByMovieId(Integer movieId) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/"+ movieId +
                        "/images?include_image_language=es&language=es"))
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
}
