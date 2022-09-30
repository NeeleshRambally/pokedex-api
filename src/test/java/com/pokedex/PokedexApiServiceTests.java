package com.pokedex;

import com.pokedex.api.model.Pokemon;
import com.pokedex.entity.pokemon.AbbilityBE;
import com.pokedex.entity.pokemon.MoveBE;
import com.pokedex.entity.pokemon.PokemonBE;
import com.pokedex.entity.pokemon.TypeBE;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import testUtils.AbstractIT;
import testUtils.RestUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@Log4j2
public class PokedexApiServiceTests extends AbstractIT {

    private final String BASE = "http://localhost:";
    private final String PATH = "/v1/pokedex";
    private final String GET = "/get/?page=0&size=1"; //Since only one entry is in THE db
    private final String GET_NAME_IMAGE = "/get/nameAndImage?page=0&size=1";

    @Test
    void testGetAll() {
        val responseFromGetAll = RestUtil.getAll(rest, BASE + port + PATH + GET, null, new ParameterizedTypeReference<List<PokemonBE>>() {
        });
        assertEquals(1, responseFromGetAll.size());
        assertEquals("TestPokemonName", responseFromGetAll.get(0).getName());

    }

    @Test
    void testGetNameAndImage() {
        val responseFromGetNameAndImage = RestUtil.getAll(rest, BASE + port + PATH + GET_NAME_IMAGE, null, new ParameterizedTypeReference<List<Pokemon>>() {
        });
        assertEquals("TestImageUrl", responseFromGetNameAndImage.get(0).getImageURL());
    }

    //TODO: Add more test cases ..


    @AfterEach
    protected void after() {
        db.pokemonRepository().deleteAll();
    }

    @BeforeEach
    protected void before() {
        PokemonBE pokemonBE = new PokemonBE();
        pokemonBE.setName("TestPokemonName");
        pokemonBE.setImageURL("TestImageUrl");
        pokemonBE.setAbbilities(getAbbility(pokemonBE));
        pokemonBE.setMoves(getMove(pokemonBE));
        pokemonBE.setTypes(getType(pokemonBE));
        db.pokemonRepository().save(pokemonBE);
        List<PokemonBE> pokemonBEList = executeSQL("select p.name from pokemon p", PokemonBE.class);
        //Verify pokemon is stored in the DB.
        assertFalse(pokemonBEList.isEmpty());
        assertEquals(1, pokemonBEList.size());
        log.info("Pokemon Saved in DB");
    }

    private Set<TypeBE> getType(PokemonBE id) {
        TypeBE typeBE = new TypeBE();
        typeBE.setPokemon(id);
        typeBE.setTypeName("typeName");
        Set<TypeBE> typeBES = new HashSet<>();
        typeBES.add(typeBE);
        return typeBES;
    }

    private Set<AbbilityBE> getAbbility(PokemonBE id) {
        AbbilityBE abbilityBE = new AbbilityBE();
        abbilityBE.setPokemon(id);
        abbilityBE.setAbbilityName("abbilityName");
        Set<AbbilityBE> abbilityBES = new HashSet<>();
        abbilityBES.add(abbilityBE);
        return abbilityBES;
    }

    private Set<MoveBE> getMove(PokemonBE id) {
        MoveBE moveBE = new MoveBE();
        moveBE.setPokemon(id);
        moveBE.setMoveName("moveName");
        Set<MoveBE> moveBES = new HashSet<>();
        moveBES.add(moveBE);
        return moveBES;
    }
}
