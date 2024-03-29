package com.dai.dai.dto.movie;

import com.dai.dai.client.movie.dto.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GetAvailableMovieGenresResponse {

    @Schema(description = "Lista de los generos disponibles.")
    List<Genre> genreList;
}
