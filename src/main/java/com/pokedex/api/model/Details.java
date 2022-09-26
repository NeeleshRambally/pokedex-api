package com.pokedex.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Details {

    @JsonProperty(value = "moves")
    private Set<Move> moves;

    @JsonProperty(value = "types")
    private Set<Type> types;

    @JsonProperty(value = "abbilities")
    private Set<Abbility> abbilities;

    @JsonProperty(value = "imageURL")
    private String imageURL;

    @JsonProperty(value = "name")
    private String name;
}
