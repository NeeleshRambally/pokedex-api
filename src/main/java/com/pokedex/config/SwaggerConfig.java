package com.pokedex.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig implements WebMvcConfigurer {
    public static final String TAG_POKEDEX_SERVICE = "v1";

    protected static final String ZA_POKEMON_MASTER = "ramballyn@gmail.com";

    public static final String TAG_POKEDEX = "getByName";
    final String[] displayTagOrder = {
            TAG_POKEDEX
    };

    @Bean
    public GroupedOpenApi landingPageApiV1() {
        return GroupedOpenApi.builder()
                .group(TAG_POKEDEX_SERVICE)
                .addOpenApiCustomiser(openApi -> {
                    openApi.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
                    openApi.setTags(getOrderedByKey(openApi.getTags(), Tag::getName, displayTagOrder));
                    openApi.info(getApiInfo());
                }).build();
    }

    private Info getApiInfo() {
        return new Info().version("1").title("Pokedex")
                .description("For Fun...")
                .contact(new Contact().email(ZA_POKEMON_MASTER).name("Neelesh Rambally"));
    }

    private <T> List<T> getOrderedByKey(List<T> origList, Function<T, String> provideKey, String[] keyOrder) {
        List<T> result = new ArrayList<>(origList);
        int i = 0;
        for (String key : keyOrder) {
            Optional<T> found = result.stream().filter(t ->
                    provideKey.apply(t).equals(key)).findFirst();
            if (found.isPresent()) {

                int indexToMove = result.indexOf(found.get());
                Collections.swap(result, i, indexToMove);
            }
            i++;
        }
        return result;
    }

}

