package com.pokedex.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

import static com.pokedex.utils.DateUtils.toDateTime;

@Mapper(componentModel = "spring")
public class PokemonDataMapper {
    @Mapping(target = "localDateTime", source = "dateTime")
    public LocalDateTime stringToLocalDateTime(String dateTime) {
        return dateTime != null ? toDateTime(dateTime) : null;
    }


}
