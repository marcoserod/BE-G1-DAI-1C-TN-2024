package com.dai.dai.dto.movie.response;

import com.dai.dai.client.movie.dto.Movie;
import com.dai.dai.client.movie.dto.MovieCast;
import com.dai.dai.client.movie.dto.MovieTrailer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class GetMovieDetailsResponse {
    @Schema(description = "Details of the obtained movie")
    Movie movie;

    @Schema(description = "Details of the obtained trailer")
    MovieTrailer movieTrailer;

    @Schema(description = "List of cast members.")
    MovieCast movieCast;

}