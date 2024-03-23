package com.dai.dai.controller.impl;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.controller.MovieController;
import com.dai.dai.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieControllerImpl implements MovieController {

    MovieService movieService;

    @GetMapping("/popular")
    @Override
    public List<Movie> getPopularMovies() throws IOException, InterruptedException {
        return movieService.movieAuth();
    }
}
