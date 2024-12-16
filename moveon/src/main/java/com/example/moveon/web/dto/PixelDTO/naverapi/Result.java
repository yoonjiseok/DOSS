package com.example.moveon.web.dto.PixelDTO.naverapi;

import com.example.moveon.web.dto.PixelDTO.naverapi.Region;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Result {

    @JsonProperty("region")
    private Region region;

}
