package com.dai.dai.dto.movie;

import com.dai.dai.client.movie.dto.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class GetMoviesResponseDto {
    @Schema(description = "Lista de peliculas obtenidas.", type = "array")
    List<Movie> movies;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String message;
}
