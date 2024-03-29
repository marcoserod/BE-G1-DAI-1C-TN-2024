package com.dai.dai.dto.movie;

import com.dai.dai.client.movie.dto.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class GetMoviesResponse {
    @Schema(description = "Lista de peliculas obtenidas.", type = "array")
    List<Movie> movies;

}
