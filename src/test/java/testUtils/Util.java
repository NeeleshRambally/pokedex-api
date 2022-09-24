package testUtils;

import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class Util {

    public static final Duration TIMEOUT_ASYNC_OPERATIONS = Duration.ofSeconds(10);

    private static final String BASE_RESOURCE_PATH = "src/test/resources";

    public static String load(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(BASE_RESOURCE_PATH, path)), StandardCharsets.UTF_8);
    }

    public static String load(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    public static String url(String base, String... parts) {
        return base + ("/" + String.join("/", parts)).replaceAll("//", "/");
    }

    public static HttpHeaders headersJson(String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);
        return headers;
    }

    public static <T> HttpEntity<T> httpJsonEntity(T obj, String bearerToken) {
        val headers = headersJson(bearerToken);
        return new HttpEntity<>(obj, headers);
    }
}
