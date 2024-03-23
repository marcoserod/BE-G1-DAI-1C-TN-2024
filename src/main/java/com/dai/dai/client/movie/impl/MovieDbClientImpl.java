package com.dai.dai.client.movie.impl;

import com.dai.dai.client.movie.MovieDbClient;
import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.client.movie.dto.Movies;
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
    List<Movie> movieListReturned = new ArrayList<Movie>();


    @Override
    public List<Movie> getPopularMovies() throws IOException, InterruptedException {
        log.info("[MovieDbClient] init");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+accesToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
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

    }
}
