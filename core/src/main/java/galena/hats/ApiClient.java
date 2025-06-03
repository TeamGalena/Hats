package galena.hats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ApiClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();

    private static final Gson GSON = new GsonBuilder()
            .setLenient()
            .create();

    public static CompletableFuture<Stream<UUID>> fetchSupporters() {
        URI uri;
        try {
            uri = new URI("https://api.galena.wiki/api/supporters");
        } catch (URISyntaxException ex) {
            return CompletableFuture.failedFuture(ex);
        }

        var request = HttpRequest.newBuilder(uri)
                .GET()
                .timeout(TIMEOUT)
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(ApiClient::handleResponse);
    }

    private static UUID parseUUID(String raw) {
        var most = new BigInteger(raw.substring(0, 16), 16);
        var least = new BigInteger(raw.substring(16, 32), 16);
        return new UUID(most.longValue(), least.longValue());
    }

    private static Stream<UUID> handleResponse(HttpResponse<String> response) {
        var status = response.statusCode();

        if (status != 200) {
            throw new IllegalStateException("API access failed with code " + status);
        }

        var json = GSON.fromJson(response.body(), JsonArray.class);
        var stream = Stream.<String>builder();
        json.forEach(it -> stream.add(it.getAsString()));
        return stream.build().map(ApiClient::parseUUID);
    }

}
