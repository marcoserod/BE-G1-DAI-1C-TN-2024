package com.dai.dai.service.impl;

import com.dai.dai.client.movie.MovieDbClient;
import com.dai.dai.service.MovieService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    MovieDbClient movieDbClient;



    @Override
    public void movieAuth() {
        log.debug("Comienza la ejecución del metodo Auth.");
        Mono<String> response = null;
        try {
            response = movieDbClient.auth("application/json","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0M2IyYWY2NzBkYWQzYjM3OGQ4N2U1MWEzOTk1YTNmOCIsInN1YiI6IjY1ZmVkOTE1MTk3ZGU0MDE2MzE2YmEzOSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.oRToqGJDnGj2-fB8JUNJ-zsaDQp1y9Hs90nn61nA1bw")
                    .doOnSuccess(resp -> log.debug("Response: {}", resp))
                    .doOnError(ex -> log.error("Exception: {}", ex.getMessage()));

        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }
        log.debug("Response: {}", response);
    }
}
