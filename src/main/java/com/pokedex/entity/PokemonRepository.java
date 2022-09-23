package com.pokedex.entity;

import com.pokedex.entity.pokemon.PokemonBE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository extends JpaRepository<PokemonBE, String> {
}
