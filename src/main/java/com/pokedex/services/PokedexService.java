package com.pokedex.services;

import com.pokedex.entity.Db;
import com.pokedex.entity.pokemon.PokemonBE;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class PokedexService {

    private final Db db;

    /**
     * Get pokemon by name
     */
    public Optional<PokemonBE> getPokemonByName(String pokemonName) {
        val pokemon = db.pokemonRepository().findById(pokemonName.toLowerCase());
        return pokemon;
    }

    /**
     * Get list of all pokemon
     *
     * */
    public List<PokemonBE> getAllPokemon() {
        val pokemonList = db.pokemonRepository().findAll();
        return pokemonList;
    }

}
