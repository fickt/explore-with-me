package com.example.apigateway.admin.event.client;

import com.example.apigateway.dto.AdminUpdateEventRequest;
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
public class AdminApiEventClient {
    private static final String API_PREFIX = "/events";
    private static final String ENDPOINT_PUBLISH_EVENT = "/{eventId}/publish";
    private static final String ENDPOINT_REJECT_EVENT = "/{eventId}/reject";
    private static final String ENDPOINT_EDIT_EVENT = "/{eventId}";
    private static final String ENDPOINT_GET_EVENTS_WITH_REQUEST_PARAMS = "/admin?users={users}&states={states}" +
            "&categories={categories}&rangeStart={rangeStart}&rangeEnd={rangeEnd}&from={from}&size={size}";

    private final RestTemplate restTemplate;

    @Autowired
    public AdminApiEventClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> publishEvent(Long eventId) {
        var params = Map.of(
          "eventId", String.valueOf(eventId)
        );
            try {
                return restTemplate.exchange(ENDPOINT_PUBLISH_EVENT, HttpMethod.PATCH, null, Object.class, params);
            } catch (
                    HttpStatusCodeException e) {
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
            }
    }

    public ResponseEntity<Object> rejectEvent(Long eventId) {
        var params = Map.of(
                "eventId", eventId
        );
        try {
            return restTemplate.exchange(ENDPOINT_REJECT_EVENT, HttpMethod.PATCH, null, Object.class, params);
        } catch (
                HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> editRequest(Long eventId, AdminUpdateEventRequest updateRequest) {
        var params = Map.of(
                "eventId", eventId
        );
        try {
            return restTemplate.exchange(ENDPOINT_EDIT_EVENT, HttpMethod.PUT, new HttpEntity<>(updateRequest, setHeaders()), Object.class, params);
        } catch (
                HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> getEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                            String rangeStart, String rangeEnd, Long from, Long size) {
        var params = Map.of(
                "users", users,
                "states", states,
                "categories", categories,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "from", from,
                "size", size
        );
        try {
            return restTemplate.exchange(ENDPOINT_GET_EVENTS_WITH_REQUEST_PARAMS, HttpMethod.GET, null, Object.class, params);
        } catch (
                HttpStatusCodeException e) {
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
