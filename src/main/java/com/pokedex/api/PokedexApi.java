package com.pokedex.api;

import com.pokedex.api.model.Details;
import com.pokedex.api.model.Pokemon;
import com.pokedex.entity.pokemon.PokemonBE;
import com.pokedex.services.PokedexService;
import com.pokedex.utils.PokemonMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.pokedex.api.PokedexApi.ENDPOINT;
import static com.pokedex.config.SwaggerConfig.TAG_POKEDEX;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ENDPOINT)
@Tag(name = TAG_POKEDEX)
@Log4j2
public class PokedexApi {

    public final PokedexService pokedexService;
    public static final String ENDPOINT = "/v1/pokedex";
    protected final PokemonMapper pokemonMapper;

    @Operation(description = "Get all pokemon the no. of pokemon that can be fetched is limited to max 100")
    @GetMapping(value = "/get", params = {"page", "size"})
    public ResponseEntity<List<PokemonBE>> getAllPokemon(
            @RequestParam(required = true, value = "page") int page,
            @RequestParam(required = true, value = "size") int siz) {
        val results = pokedexService.getAllPokemon(page, siz);
        if (results.isEmpty()) {
            log.info("no pokemon found..");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(results.getContent());
    }


    @Operation(description = "Get name and image url of pokemon - the no. of pokemon that can be fetched is limited to max 100")
    @GetMapping(value = "/get/nameAndImage", params = {"page", "size"})
    public ResponseEntity<List<Pokemon>> getAllPokemonNamesAndImageUrl(
            @RequestParam(required = true, value = "page") int page,
            @RequestParam(required = true, value = "size") int size) {
        val results = pokedexService.getAllPokemon(page, size);
        if (results.isEmpty()) {
            log.info("no pokemon found..");
            return ResponseEntity.notFound().build();
        }
        List<Pokemon> pokemons = pokemonMapper.toPokemon(results.getContent());
        return ResponseEntity.ok(pokemons);
    }

    @Operation(description = "Get pokemons details")
    @GetMapping(value = "/get/details", params = {"pokemonName"})
    public ResponseEntity<Details> getPokemonDetailsByName(@RequestParam(required = true, value = "pokemonName") String name) {
        val results = pokedexService.getPokemonByName(name);
        if (!results.isPresent()) {
            log.info("no pokemon found..");
            return ResponseEntity.notFound().build();
        }
        Details details = pokemonMapper.getDetails(results.get());
        return ResponseEntity.ok(details);
    }
}
