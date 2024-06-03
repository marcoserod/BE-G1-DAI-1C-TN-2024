package com.dai.dai.dto.movie.response;

import com.dai.dai.client.movie.dto.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class GetMovieDetailsResponse {
    @Schema(description = "Details of the obtained movie")
    GetMovieByIdResponse movie;

    @Schema(description = "List of available genres")
    List<Genre> genreList;

    @Schema(description = "Details of the obtained trailer")
    MovieTrailer movieTrailer;

    @Schema(description = "List of cast members.")
    MovieCast movieCast;

    @Schema(description = "List od images.")
    ImageList imageList;

}