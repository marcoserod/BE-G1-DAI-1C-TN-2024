package com.dai.dai.dto.movie;

import com.dai.dai.client.movie.dto.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class GetMovieDetailsResponse {
    @Schema(description = "Details of the obtained movie")
    Movie movie;

}