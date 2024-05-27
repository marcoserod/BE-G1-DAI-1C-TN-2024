package com.dai.dai.dto.movie.response;

import com.dai.dai.client.movie.dto.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@Data
@AllArgsConstructor
public class GetMoviesResponse {
    @Schema(description = "List of obtained movies", type = "array")
    List<Movie> movies;
    @Schema(description = "Metadata")
    ListMetadata metadata;

}
