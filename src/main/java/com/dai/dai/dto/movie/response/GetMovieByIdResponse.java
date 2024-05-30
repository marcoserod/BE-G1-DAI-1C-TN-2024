package com.dai.dai.dto.movie.response;

import com.dai.dai.client.movie.dto.Genre;
import com.dai.dai.client.movie.dto.Movie;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GetMovieByIdResponse {
    private Long id;
    private String title;
    private String poster_path;// (foto)
    private String overview;
    private String release_date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String first_air_date;
    private Double vote_average;
    private Long vote_count;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String media_type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Movie> known_for;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Genre> genres;
    @JsonProperty(value = "runtime")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long duration;
    @JsonProperty(value = "tagline")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String subtitle;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean adult;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String backdrop_path;
}
