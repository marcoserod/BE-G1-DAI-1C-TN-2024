package com.dai.dai.client.movie.dto;

import lombok.Data;

@Data
public class PostMovieRatingResponse {

    private Boolean success;
    private Integer status_code;
    private String status_message;

}
