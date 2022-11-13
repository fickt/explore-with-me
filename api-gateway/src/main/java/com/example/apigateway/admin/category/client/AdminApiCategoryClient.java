package com.example.apigateway.admin.category.client;

import com.example.apigateway.dto.category.CategoryDto;
import com.example.apigateway.dto.category.NewCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;
import java.util.List;

@Service
public class AdminApiCategoryClient {
    private final RestTemplate restTemplate;
    private static final String API_PREFIX = "/categories";
    private static final String ENDPOINT_BLANK = "";

    @Autowired
    public AdminApiCategoryClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .setConnectTimeout(Duration.ofMillis(5000))
                .setReadTimeout(Duration.ofMillis(5000))
                .build();
    }


    public ResponseEntity<Object> addCategory(NewCategoryDto categoryDto) {
        try {
            return restTemplate.exchange(ENDPOINT_BLANK, HttpMethod.POST, new HttpEntity<>(categoryDto, setHeaders()), Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> editCategory(CategoryDto categoryDto) {
        try {
            return restTemplate.exchange(ENDPOINT_BLANK, HttpMethod.PATCH, new HttpEntity<>(categoryDto, setHeaders()), Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public void deleteCategory(Long categoryId) {
        restTemplate.delete("/" + categoryId);
    }

    private HttpHeaders setHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }
}
