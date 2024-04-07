package com.dai.dai.dto.user;

import com.dai.dai.client.movie.dto.MovieTrailer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class PostUsersResponse {
    @Schema(description = "Detail of created user")
    UserDto userDto;
}