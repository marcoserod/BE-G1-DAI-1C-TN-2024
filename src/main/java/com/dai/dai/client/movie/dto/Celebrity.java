package com.dai.dai.client.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Celebrity {
    private boolean adult;
    private int gender;
    private int id;
    @JsonProperty(value = "known_for_department")
    private String knownForDepartment;
    private String name;
    @JsonProperty(value = "original_name")
    private String originalName;
    private double popularity;
    @JsonProperty(value = "profile_path")
    private String profilePath;
    @JsonProperty(value = "cast_id")
    private int castId; // Este campo podría ser específico para el elenco
    private String character; // Este campo podría ser específico para el elenco
    @JsonProperty(value = "credit_id")
    private String creditId;
    private int order; // Este campo podría ser específico para el elenco
    private String department; // Este campo podría ser específico para el equipo
    private String job; // Este campo podría ser específico para el equipo

}
