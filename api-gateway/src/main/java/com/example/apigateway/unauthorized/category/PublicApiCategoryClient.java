package com.example.apigateway.unauthorized.category;

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
public class PublicApiCategoryClient {
    private static final String ENDPOINT_GET_CATEGORIES_WITH_PARAMS = "?from={from}&size={size}";
    private static final String ENDPOINT_GET_CATEGORY_BY_ID = "/{categoryId}";
    private static final String API_PREFIX = "/categories";
    private final RestTemplate restTemplate;

    @Autowired
    public PublicApiCategoryClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> getCategories(Long from, Long size) {
        try {
            var params = Map.of(
                    "from", from,
                    "size", size
            );
            return restTemplate.exchange(ENDPOINT_GET_CATEGORIES_WITH_PARAMS, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> getCategoryById(Long categoryId) {
        try {
            var params = Map.of(
                    "categoryId", categoryId
            );
            return restTemplate.exchange(ENDPOINT_GET_CATEGORY_BY_ID, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

}
