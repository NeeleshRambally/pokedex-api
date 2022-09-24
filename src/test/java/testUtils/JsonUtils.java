package testUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.shaded.org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Log4j2
@Configuration
public class JsonUtils {

    private static JsonUtils instance = null;

    @Getter
    private ObjectMapper mapper;

    @Bean
    @Primary
    public ObjectMapper pipeObjectMapper() {
        mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        mapper.registerModule(new EnumSerializerModule());
        return mapper;
    }

    public static JsonUtils instance() {
        if (instance ==  null) {
            instance = new JsonUtils();
            instance.pipeObjectMapper();
        }
        return instance;
    }

    public String toJson(Object object) throws JsonProcessingException {
        return StringEscapeUtils.unescapeJava(mapper.writeValueAsString(object));
    }

    public String toJsonPretty(Object object) throws JsonProcessingException {
        return StringEscapeUtils.unescapeJava(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
    }

    public JsonNode toJsonObject(String json) throws JsonProcessingException {
        return mapper.readTree(json);
    }

    /**
     * Use this method to convert Json string to an object
     * @param json mandatory.
     * @return object.
     */
    public <T> T fromJson(String json, Class<T> classOfT) throws IOException {
        return json != null && classOfT != null ?  mapper.readValue(json, classOfT) : null;
    }

    public static <T> Optional<T> getFirstOrEmpty(Collection<T> collection) {
        return collection.isEmpty() ? Optional.empty() : Optional.of(collection.iterator().next());
    }

    /**
     * For usage with stream API to remove duplicates by certain attribute inside a collection.
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static <E extends Enum<E>> boolean isValidEnumValueNotNullable(Class<E> enumType, String value) {
        return isValidEnumValue(enumType, value, false);
    }

    public static <E extends Enum<E>> boolean isValidEnumValueNullable(Class<E> enumType, String value) {
        return isValidEnumValue(enumType, value, true);
    }

    @SuppressWarnings("squid:S1166") // Exceptions here are okay.
    private static <E extends Enum<E>> boolean isValidEnumValue(Class<E> enumType, String value, boolean nullable) {

        if (nullable && value == null) {
            return true;
        }

        try {
            Enum.valueOf(enumType, value);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            log.debug(e);
            return false;
        }

    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isNullOrEmpty(String string) {
        return isNull(string) || string.isEmpty() || isBlank(string);
    }

    public static boolean isBlank(String string) {
        return isNull(string) || string.trim().length() == 0;
    }

}
