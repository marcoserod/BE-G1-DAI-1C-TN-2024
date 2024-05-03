package com.dai.dai.client.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieCast {
    private int id;
    private List<Celebrity> cast;
    private List<Celebrity> crew;
}
