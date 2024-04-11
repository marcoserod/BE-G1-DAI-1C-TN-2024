package com.dai.dai.dto.movie.response;

import com.dai.dai.client.movie.dto.MovieTrailer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class GetMovieTrailerDetailsResponse {
    @Schema(description = "Details of the obtained trailer")
    MovieTrailer movieTrailer;

}