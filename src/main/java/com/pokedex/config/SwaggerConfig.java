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

    public static final String TAG_PIPE_SERVICE_V1 = "v1";
    public static final String TAG_ORDERS = "Order Service";
    public static final String TAG_PLANNED_ORDERS = "Planned Orders Service";
    public static final String TAG_REVISION_MANAGER = "Revision Manager Service";
    public static final String TAG_DICE_TOOLS = "Dice Service";
    public static final String TAG_CLEANUP = "Clean Up Service";
    public static final String TAG_CONFIGURABLE_PROPERTY_SERVICE = "Configurable Property Service";
    public static final String TAG_HEALTH = "Health Service";
    public static final String TAG_MASTER_DATA = "Master Data Service";
    public static final String TAG_SWAP = "Swap Service";
    protected static final String ZA_PIPE_LIST_BMW_COM = "ZA-PIPE@list.bmw.com";

    final String[] displayTagOrder = {
        TAG_ORDERS,
        TAG_PLANNED_ORDERS,
        TAG_REVISION_MANAGER,
        TAG_DICE_TOOLS,
        TAG_CLEANUP,
        TAG_CONFIGURABLE_PROPERTY_SERVICE,
        TAG_MASTER_DATA,
        TAG_HEALTH
    };



    @Bean
    public GroupedOpenApi landingPageApiV1() {
        return GroupedOpenApi.builder()
                .group(TAG_PIPE_SERVICE_V1)
                .addOpenApiCustomiser(openApi -> {
                    openApi.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
                    openApi.setTags(getOrderedByKey(openApi.getTags(), Tag::getName, displayTagOrder));
                    openApi.info(getApiInfo());
                }).build();
    }

    private Info getApiInfo() {
        return new Info().version("12").title("PIPE").description("PIPE Central Ordering Integrated Pipeline").contact(new Contact().email(ZA_PIPE_LIST_BMW_COM).name("Developer Team"));
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

