package com.dai.dai.controller;


import com.dai.dai.client.movie.dto.Movie;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public interface MovieController {

    List<Movie> getPopularMovies() throws IOException, InterruptedException;
}
