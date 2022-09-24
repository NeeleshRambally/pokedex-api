package com.pokedex.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class Type {
    
    @JsonProperty(value = "typeName")
    private String typeName;
}
