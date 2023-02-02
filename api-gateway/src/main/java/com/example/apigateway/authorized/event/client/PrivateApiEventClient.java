package com.example.apigateway.authorized.event.client;

import com.example.apigateway.dto.eventdto.NewEventDto;
import com.example.apigateway.dto.eventdto.UpdateEventRequest;
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
import java.util.List;
import java.util.Map;

@Service
public class PrivateApiEventClient {

    private static final String API_PREFIX = "/events";
    private static final String ENDPOINT_GET_EVENT_BY_ID = "/{eventId}/users/{userId}";
    private static final String ENDPOINT_ADD_EVENT = "/users/{userId}";
    private static final String ENDPOINT_EDIT_EVENT = "/users/{userId}";
    private static final String ENDPOINT_GET_USER_EVENTS = "/users/{userId}";
    private static final String ENDPOINT_CANCEL_EVENT = "/{eventId}/users/{userId}/cancel";
    private static final String ENDPOINT_GET_USER_REQUESTS_IN_EVENT = "/{eventId}/users/{userId}/requests";
    private static final String ENDPOINT_CONFIRM_REQUEST = "/{eventId}/users/{userId}/requests/{reqId}/confirm";
    private static final String ENDPOINT_REJECT_REQUEST = "/{eventId}/users/{userId}/requests/{reqId}/reject";
    private final RestTemplate restTemplate;

    @Autowired
    public PrivateApiEventClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> getEventById(Long userId, Long eventId) {
        try {
            var params = Map.of(
                    "userId", userId,
                    "eventId", eventId
            );
            return restTemplate.exchange(ENDPOINT_GET_EVENT_BY_ID, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> addEvent(Long userId, NewEventDto eventDto) {
        eventDto.setCreatedOn(LocalDateTime.now());
        try {
            var params = Map.of("userId", userId);
            return restTemplate.exchange(ENDPOINT_ADD_EVENT, HttpMethod.POST, new HttpEntity<>(eventDto, setHeaders()), Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> editEvent(Long userId, UpdateEventRequest eventRequest) {
        try {
            var params = Map.of("userId", userId);
            return restTemplate.exchange(ENDPOINT_EDIT_EVENT, HttpMethod.PATCH, new HttpEntity<>(eventRequest, setHeaders()), Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> getEventsOfUser(Long userId) {
        try {
            var params = Map.of("userId", String.valueOf(userId));
            return restTemplate.exchange(ENDPOINT_GET_USER_EVENTS, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> cancelEventOfUser(Long userId, Long eventId) {
        try {
            var params = Map.of(
                    "userId", userId,
                    "eventId", eventId
            );
            return restTemplate.exchange(ENDPOINT_CANCEL_EVENT, HttpMethod.PATCH, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> getRequestsOfUserInEvent(Long userId, Long eventId) {
        try {
            var params = Map.of(
                    "userId", userId,
                    "eventId", eventId
            );
            return restTemplate.exchange(ENDPOINT_GET_USER_REQUESTS_IN_EVENT, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> confirmRequestOfUser(Long userId, Long eventId, Long reqId) {
        try {
            var params = Map.of(
                    "userId", userId,
                    "eventId", eventId,
                    "reqId", reqId
            );
            return restTemplate.exchange(ENDPOINT_CONFIRM_REQUEST, HttpMethod.PATCH, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> rejectRequestOfUser(Long userId, Long eventId, Long reqId) {
        try {
            var params = Map.of(
                    "userId", userId,
                    "eventId", eventId,
                    "reqId", reqId
            );
            return restTemplate.exchange(ENDPOINT_REJECT_REQUEST, HttpMethod.PATCH, null, Object.class, params);
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
