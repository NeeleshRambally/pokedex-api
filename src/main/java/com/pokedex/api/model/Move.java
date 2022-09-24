package com.pokedex.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Move {

    @JsonProperty(value = "moveName")
    private String moveName;
}
