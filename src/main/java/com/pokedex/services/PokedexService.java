package com.pokedex.services;

import com.pokedex.entity.Db;
import com.pokedex.entity.pokemon.PokemonBE;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
     * Paginated , limited to 100 at a time
     */
    public Page<PokemonBE> getAllPokemon(int page, int limit) {
        if (limit > 100) {
            log.info("limit exceeded , defaulting to 100");
            limit = 100;
        }
        val pokemonPageList = db.pokemonRepository().findAll(PageRequest.of(page, limit));
        return pokemonPageList;
    }

}
