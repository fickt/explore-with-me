package com.example.apigateway.unauthorized.rating;

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
public class PublicApiRatingClient {
    private static final String API_PREFIX = "/ratings";
    private static final String ENDPOINT_GET_EVENTS_WITH_RATING = "?sort={sort}&size={size}";
    private final RestTemplate restTemplate;

    @Autowired
    public PublicApiRatingClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> getSortedEventsWithRating(String sort, Long size) {
        try {
            var params = Map.of(
                    "sort", sort,
                    "size", size
            );
            return restTemplate.exchange(ENDPOINT_GET_EVENTS_WITH_RATING, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }
}
