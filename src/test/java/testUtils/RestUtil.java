package testUtils;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static testUtils.Util.headersJson;

@Log4j2
public class RestUtil {

    public static <T> Optional<T> get(TestRestTemplate template, String url, String bearerToken, ParameterizedTypeReference<T> parameterizedTypeReference) {
        return get(template.exchange(url, HttpMethod.GET, new HttpEntity<>(headersJson(bearerToken)), parameterizedTypeReference));
    }

    public static <T> Optional<T> get(TestRestTemplate template, String url, String bearerToken, Class<T> clazz) {
        return get(template.exchange(url, HttpMethod.GET, new HttpEntity<>(headersJson(bearerToken)), clazz));
    }

    private static <T> Optional<T> get(ResponseEntity<T> response) {

        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        }

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            return Optional.empty();
        }

        assertFalse(true, "Get request failed with response status: " + response.getStatusCode());
        return Optional.empty();
    }

    public static ResponseEntity<String> getEntityWithStringBody(TestRestTemplate template, String url, String bearerToken, String filter, Integer limit, Integer page) {
        val uri = buildFilterUrl(url, filter, limit, page);
        return template.exchange(uri, HttpMethod.GET, new HttpEntity<>(headersJson(bearerToken)), String.class);
    }

    public static ResponseEntity<String> getResponseFromUrl(TestRestTemplate template, String url, String bearerToken) {
        return template.exchange(url, HttpMethod.GET, new HttpEntity<>(headersJson(bearerToken)), String.class);
    }

    private static URI buildFilterUrl(String url, String filter, Integer limit, Integer page) {
        val uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
        if (filter != null) {
            uriComponentsBuilder.queryParam("filter", filter);
        }
        if (limit != null) {
            uriComponentsBuilder.queryParam("limit", limit);
        }
        if (page != null) {
            uriComponentsBuilder.queryParam("page", page);
        }
        return uriComponentsBuilder.build().toUri();
    }

    public static <T> List<T> getAll(TestRestTemplate template, String url, String bearerToken, ParameterizedTypeReference<List<T>> responseType) {
        val response = template.exchange(url, HttpMethod.GET, new HttpEntity<>(headersJson(bearerToken)), responseType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    @SneakyThrows
    public static <R> ResponseEntity<List<R>> filterHttp(
            TestRestTemplate template, String url, String bearerToken, String filter, Integer limit, Integer page, ParameterizedTypeReference<List<R>> responseType) {
        val uri = buildFilterUrl(url, filter, limit, page);
        val response = template.exchange(uri, HttpMethod.GET, new HttpEntity<>(headersJson(bearerToken)), responseType);
        return response;
    }

    @SneakyThrows
    public static <R> List<R> filter(TestRestTemplate template, String url, String bearerToken, String filter, Integer limit, Integer page, ParameterizedTypeReference<List<R>> responseType) {
        val response = filterHttp(template, url, bearerToken, filter, limit, page, responseType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    @SneakyThrows
    public static <R> List<R> filterAndCount(TestRestTemplate template, String url, String bearerToken, String filter, String[] groupBy, ParameterizedTypeReference<List<R>> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (filter != null) {
            builder.queryParam("filter", filter);
        }
        if (groupBy != null) {
            builder.queryParam("groupby", groupBy);
        }
        val uri = builder.build().toUri();
        val response = template.exchange(uri, HttpMethod.GET, new HttpEntity<>(headersJson(bearerToken)), responseType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    public static void delete(TestRestTemplate template, String url, String bearerToken) {
        template.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headersJson(bearerToken)), String.class);
    }
}
