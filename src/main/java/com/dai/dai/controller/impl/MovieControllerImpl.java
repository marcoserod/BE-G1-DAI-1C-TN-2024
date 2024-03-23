package com.dai.dai.controller.impl;

import com.dai.dai.controller.MovieController;
import com.dai.dai.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/movie")
public class MovieControllerImpl implements MovieController {

    MovieService movieService;

    @GetMapping("/auth")
    @Override
    public void movieAuth() {
        movieService.movieAuth();
    }
}
