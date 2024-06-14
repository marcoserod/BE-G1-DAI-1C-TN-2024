package com.dai.dai.client.movie.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostMovieRatingRequest {
    private float value;
}
