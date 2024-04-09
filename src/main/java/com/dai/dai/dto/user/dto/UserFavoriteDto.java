package com.dai.dai.dto.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserFavoriteDto {

    private String film_id;
    private String user_id;
}
