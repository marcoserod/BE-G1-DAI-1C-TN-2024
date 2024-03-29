package com.dai.dai.client.movie.impl;

import com.dai.dai.client.movie.MovieDbClient;
import com.dai.dai.client.movie.dto.Genre;
import com.dai.dai.client.movie.dto.GenresResponse;
import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.client.movie.dto.MovieTrailer;
import com.dai.dai.client.movie.dto.Movies;
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
    public List<Movie> getPopularMovies() throws IOException, InterruptedException {
        log.info("[MovieDbClient] getPopularMovies init");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return this.getMovieListRequest(request);
    }

    @Override
    public List<Movie> getNowPlaying() throws IOException, InterruptedException {
        log.info("[MovieDbClient] getNowPlaying init");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=1"))
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
        try {
            movie = objectMapper.readValue(response.body(), Movie.class);
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error al consultar TMDB Api.");
        }
        return movie;
    }

    @Override
    public List<Genre> getAvailableMovieGenres() throws IOException, InterruptedException {
        log.info("[MovieDbClient] getAvailableMovieGenres.");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/genre/movie/list?language=en"))
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
            throw new RuntimeException("Ocurrió un error al consultar TMDB Api.");
        }
        if (!genresResponse.getGenres().isEmpty()){
            return genresResponse.getGenres();
        } else {
            throw new RuntimeException("Ocurrió un error al recuperar los generos solicitados.");
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
                        movie.setOriginal_title(oneMovie.getOriginal_title());
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
            throw new RuntimeException("Ocurrió un error al consultar TMDB Api.");
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
        var movieVideo = new MovieTrailer();

        try {
            var objectMapper = new ObjectMapper();
            var jsonResponse = objectMapper.readTree(response.body());
            var resultsNode = jsonResponse.get("results");
            if (resultsNode.isArray() && resultsNode.size() > 0) {
                for (JsonNode result : resultsNode) {
                    if (result.get("type").asText().equals("Trailer") &&
                            result.get("site").asText().equals("YouTube")) {
                        var link = "https://www.youtube.com/watch?v=" + result.get("key").asText();
                        movieVideo.setLink(link);
                        return movieVideo;
                    }
                }
            }
            throw new RuntimeException("No se encontraron trailers para la película con ID: " + movieId);

        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error al consultar TMDB Api.", e);
        }
    }
}
