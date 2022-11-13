package com.example.apigateway.unauthorized.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class PublicApiCompilationClient {
    private static final String API_PREFIX = "/compilations";
    private static final String ENDPOINT_GET_COMPILATION_BY_ID = "/{compId}";
    private static final String ENDPOINT_GET_COMPILATIONS_WITH_PARAMS = "?pinned={pinned}&from={from}&size={size}";
    private final RestTemplate restTemplate;


    @Autowired
    public PublicApiCompilationClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> getCompilations(Boolean pinned, Long from, Long size) {
        try {
            var params = Map.of(
                    "pinned", pinned,
                    "from", from,
                    "size", size
            );
            return restTemplate.exchange(ENDPOINT_GET_COMPILATIONS_WITH_PARAMS, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> getCompilationById(Long compId) {
        try {
            var params = Map.of(
                    "compId", String.valueOf(compId)
            );
            return restTemplate.exchange(ENDPOINT_GET_COMPILATION_BY_ID, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }
}
