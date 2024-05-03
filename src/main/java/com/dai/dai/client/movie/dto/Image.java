package com.dai.dai.client.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Image {

    @JsonProperty(value = "file_path" )
    String filePath;
}
