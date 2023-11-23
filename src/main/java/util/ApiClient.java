package util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.NonNull;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstraction for Http Client using Apache Http Library reducing high level dependencies.
 * @implNote Lots to improve. Specially exception management.
 */
public final class ApiClient {

    public static <T> T callGet(@NonNull String apiEndpoint, @NonNull Map<String, String> queryParams,
                                @NonNull Map<String, String> headerParams, @NonNull TypeReference<T> tClass) {

        HttpUriRequest request = prepareRequestBuilder(apiEndpoint, queryParams, headerParams, HttpGet::new);
        return performRequestCall(request, tClass);
    }


    public static <T, K> T callPost(@NonNull String apiEndpoint, @NonNull Map<String, String> queryParams,
                                    @NonNull Map<String, String> headerParams, @NonNull K requestBody,
                                    @NonNull TypeReference<T> tClass) {

        HttpUriRequest request = prepareRequestBuilder(apiEndpoint, queryParams, headerParams, uri -> {
            HttpPost httpPost = new HttpPost(uri);
            try {
                httpPost.setEntity(new StringEntity(ThingMapper.asString(requestBody)));
                return httpPost;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return performRequestCall(request, tClass);

    }

    private static HttpUriRequest prepareRequestBuilder(String apiEndpoint, Map<String, String> queryParams,
                                                        Map<String, String> headerParams,
                                                        Function<URI, HttpUriRequest> method) {
        URI uri = URI.create(apiEndpoint);

        if (!queryParams.isEmpty()) {
            uri = appendQueryParams(uri, queryParams);
        }

        HttpUriRequest request = method.apply(uri);
        headerParams.forEach(request::setHeader);

        request.setHeader("Accept", "*/*");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("User-Agent", "ACE/4.18");

        return request;
    }

    private static URI appendQueryParams(URI uri, Map<String, String> queryParams) {
        String query = String.join("&", queryParams.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).toArray(String[]::new));

        return URI.create(uri.toString() + "?" + query);
    }

    private static <T> T performRequestCall(HttpUriRequest request, TypeReference<T> typeReference) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CloseableHttpResponse response = httpClient.execute(request);

            return ThingMapper.readValue(EntityUtils.toString(response.getEntity()), typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
