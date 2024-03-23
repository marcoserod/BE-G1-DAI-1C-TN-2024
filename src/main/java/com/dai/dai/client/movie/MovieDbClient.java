package com.dai.dai.client.movie;


import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


public interface MovieDbClient {
    WebClient webClient = WebClient.create("https://api.themoviedb.org/3");

    default Mono<String> auth(String acceptHeader, String authorizationHeader) {
        return webClient.get()
                .uri("/authentication")
                .header(HttpHeaders.ACCEPT, acceptHeader)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .retrieve()
                .bodyToMono(String.class);
    }
}
