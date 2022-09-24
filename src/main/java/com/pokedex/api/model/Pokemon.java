package com.pokedex.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Pokemon {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "imageURL")
    private String imageURL;
}
