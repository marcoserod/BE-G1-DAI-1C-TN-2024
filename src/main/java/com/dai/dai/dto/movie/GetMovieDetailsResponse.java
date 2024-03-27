package com.dai.dai.dto.movie;

import com.dai.dai.client.movie.dto.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class GetMovieDetailsResponse {
    @Schema(description = "Detalle de la pelicula obtenida")
    Movie movie;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String message;
}