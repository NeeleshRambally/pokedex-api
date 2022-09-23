package com.pokedex.api;

import com.pokedex.entity.pokemon.PokemonBE;
import com.pokedex.services.PokedexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pokedex.api.PokedexApi.ENDPOINT;
import static com.pokedex.config.SwaggerConfig.TAG_POKEDEX;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ENDPOINT)
@Tag(name = TAG_POKEDEX)
@Log4j2
public class PokedexApi {

    public  final PokedexService pokedexService;
    public static final String ENDPOINT = "/v1/pokedex";

    @Operation(description = "Get pokemon by name.")
    @GetMapping("/getByName/{name}")
    public ResponseEntity<PokemonBE> getPokemon(@PathVariable("name") String name) {
        val pokemon = pokedexService.getPokemonByName(name);
        if(pokemon.isPresent()){
            val poke = pokemon.get();
            return  ResponseEntity.ok(poke);
        }else{
            log.info("pokemon {} not found",name);
            return ResponseEntity.notFound().build();
        }
    }

}
