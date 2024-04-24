package com.dai.dai.client.movie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    private Long id;
    @JsonProperty(value = "original_name")
    private String title;
    private String poster_path;// (foto)
    private String overview;
    @JsonProperty(value = "first_air_date")
    private String release_date;
    private Double vote_average;
    private Long vote_count;

}
