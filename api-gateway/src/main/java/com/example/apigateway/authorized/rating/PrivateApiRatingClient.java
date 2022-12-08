package com.example.apigateway.authorized.rating;

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
public class PrivateApiRatingClient {
    private static final String API_PREFIX = "/ratings";
    private static final String ENDPOINT_LIKE_EVENT = "/users/{userId}/events/{eventId}/like";
    private static final String ENDPOINT_DISLIKE_EVENT = "/users/{userId}/events/{eventId}/dislike";
    private final RestTemplate restTemplate;

    @Autowired
    public PrivateApiRatingClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> likeEvent(Long userId, Long eventId) {
        try {
            var params = Map.of(
                    "userId", userId,
                    "eventId", eventId
            );
            return restTemplate.exchange(ENDPOINT_LIKE_EVENT, HttpMethod.PUT, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> dislikeEvent(Long userId, Long eventId) {
        try {
            var params = Map.of(
                    "userId", userId,
                    "eventId", eventId
            );
            return restTemplate.exchange(ENDPOINT_DISLIKE_EVENT, HttpMethod.PUT, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }
}
