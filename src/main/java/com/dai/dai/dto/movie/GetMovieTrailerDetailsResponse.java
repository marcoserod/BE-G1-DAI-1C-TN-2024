package com.dai.dai.dto.movie;

import com.dai.dai.client.movie.dto.MovieTrailer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class GetMovieTrailerDetailsResponse {
    @Schema(description = "Detalle del trailer obtenido")
    MovieTrailer movieTrailer;

}