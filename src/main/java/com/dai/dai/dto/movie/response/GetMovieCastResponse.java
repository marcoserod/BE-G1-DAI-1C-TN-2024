package com.dai.dai.dto.movie.response;

import com.dai.dai.client.movie.dto.MovieCast;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetMovieCastResponse {
    MovieCast movieCast;
}
