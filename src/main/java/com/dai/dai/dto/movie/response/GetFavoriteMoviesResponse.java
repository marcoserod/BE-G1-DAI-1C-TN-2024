package com.dai.dai.dto.movie.response;

import com.dai.dai.client.movie.dto.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public class GetFavoriteMoviesResponse {
    @Schema(description = "List of obtained movies", type = "array")
    List<GetMovieByIdResponse> movies;
    @Schema(description = "Metadata")
    ListMetadata metadata;
}
