package com.example.apigateway.admin.compilation.client;

import com.example.apigateway.dto.compilationdto.NewCompilationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

@Service
public class AdminApiCompilationClient {
    private final RestTemplate restTemplate;
    private static final String API_PREFIX = "/compilations";
    private static final String ENDPOINT_DELETE_COMPILATION = "/{compId}";
    private static final String ENDPOINT_DELETE_EVENT_FROM_COMPILATION = "/{compId}/events/{eventId}";
    private static final String ENDPOINT_ADD_EVENT_TO_COMPILATION = "/{compId}/events/{eventId}";
    private static final String ENDPOINT_PIN_COMPILATION = "/{compId}/pin";
    private static final String ENDPOINT_UNPIN_COMPILATION = "/{compId}/pin";
    private static final String ENDPOINT_BLANK = "";

    @Autowired
    public AdminApiCompilationClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> addCompilation(NewCompilationDto compilationDto) {
        try {
            return restTemplate.exchange(ENDPOINT_BLANK, HttpMethod.POST, new HttpEntity<>(compilationDto, setHeaders()), Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> deleteCompilation(Long compId) {
        var params = Map.of(
                "compId", compId
        );
        try {
            return restTemplate.exchange(ENDPOINT_DELETE_COMPILATION, HttpMethod.DELETE, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> deleteEventFromCompilation(Long compId, Long eventId) {
        var params = Map.of(
                "compId", compId,
                "eventId", eventId
        );
        try {
            return restTemplate.exchange(ENDPOINT_DELETE_EVENT_FROM_COMPILATION, HttpMethod.DELETE, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> addEventToCompilation(Long compId, Long eventId) {
        var params = Map.of(
                "compId", compId,
                "eventId", eventId
        );
        try {
            return restTemplate.exchange(ENDPOINT_ADD_EVENT_TO_COMPILATION, HttpMethod.PATCH, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> pinCompilation(Long compId) {
        var params = Map.of(
                "compId", compId
        );
        try {
            return restTemplate.exchange(ENDPOINT_PIN_COMPILATION, HttpMethod.PATCH, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> unpinCompilation(Long compId) {
        var params = Map.of(
                "compId", String.valueOf(compId)
        );
        try {
            return restTemplate.exchange(ENDPOINT_UNPIN_COMPILATION, HttpMethod.DELETE, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    private HttpHeaders setHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }
}
