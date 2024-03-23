package com.dai.dai.service;

import com.dai.dai.client.movie.dto.Movie;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    List<Movie> movieAuth() throws IOException, InterruptedException;
}
