package com.pokedex.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Abbility {

    @JsonProperty(value = "abbilityName")
    private String abbilityName;
}
