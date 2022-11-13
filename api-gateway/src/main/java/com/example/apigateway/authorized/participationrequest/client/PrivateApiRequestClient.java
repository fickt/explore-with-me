package com.example.apigateway.authorized.participationrequest.client;

import com.example.apigateway.dto.participationrequestdto.NewParticipationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;

@Service
public class PrivateApiRequestClient {
    private static final String API_PREFIX = "/requests";
    private static final String ENDPOINT_BLANK = "";
    private static final String ENDPOINT_GET_ALL_USER_REQUESTS = "/users/{userId}";
    private static final String ENDPOINT_CANCEL_REQUEST = "/{requestId}/users/{userId}/cancel";
    private final RestTemplate restTemplate;

    @Autowired
    public PrivateApiRequestClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> addParticipationRequest(Long userId, Long eventId) {
        var requestDto = new NewParticipationRequestDto();
        requestDto.setRequester(userId);
        requestDto.setEvent(eventId);
        requestDto.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        try {
            return restTemplate.exchange(ENDPOINT_BLANK, HttpMethod.POST, new HttpEntity<>(requestDto, setHeaders()), Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> getAllParticipationRequestsOfUser(Long userId) {
        var params = Map.of("userId", String.valueOf(userId));
        try {
            return restTemplate.exchange(ENDPOINT_GET_ALL_USER_REQUESTS, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> cancelParticipationRequestOfUser(Long userId, Long requestId) {
        var params = Map.of(
                "userId", userId,
                "requestId", requestId
        );
        try {
            return restTemplate.exchange(ENDPOINT_CANCEL_REQUEST, HttpMethod.PATCH, null, Object.class, params);
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
