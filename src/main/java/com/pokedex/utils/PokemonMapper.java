package com.pokedex.utils;

import com.pokedex.api.model.Details;
import com.pokedex.api.model.Pokemon;
import com.pokedex.entity.pokemon.PokemonBE;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PokemonDataMapper.class})
@Log4j2
public abstract class PokemonMapper {


    public abstract Pokemon toPokemon(PokemonBE pokemonBE);

    public abstract List<Pokemon> toPokemon(List<PokemonBE> pokemonBEs);

    public abstract Details getDetails(PokemonBE pokemonBE);
}
