package com.example.apigateway.unauthorized.event.client;

import com.example.apigateway.endpointhit.EndpointHit;
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
public class PublicApiEventClient {
    private static final String API_PREFIX = "/events";
    private static final String ENDPOINT_GET_CATEGORIES_WITH_PARAMS = "?categories={categories}&text={text}&paid={paid}" +
            "&rangeStart={rangeStart}&rangeEnd={rangeEnd}&onlyAvailable={onlyAvailable}&sort={sort}&from={from}&size={size}";
    private static final String ENDPOINT_GET_EVENT_BY_ID = "/{eventId}";


    private final RestTemplate restTemplate;

    @Autowired
    public PublicApiEventClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> getEvents(List<Integer> categories, String text, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Long from, Long size) {
        var params = Map.of(
                "categories", categories,
                "text", text,
                "paid", paid,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "onlyAvailable", onlyAvailable,
                "sort", sort,
                "from", from,
                "size", size
        );
        try {
            return restTemplate.exchange(ENDPOINT_GET_CATEGORIES_WITH_PARAMS,
                    HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> getEventById(Long eventId, String ip, String URI) {
        var hit = makeEndpointHit(ip, URI, "ewm-main-service", eventId);

        var params = Map.of(
                "eventId", eventId
        );
        try {
            return restTemplate.exchange(ENDPOINT_GET_EVENT_BY_ID, HttpMethod.POST, new HttpEntity<>(hit, setHeaders()), Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    private EndpointHit makeEndpointHit(String ip, String URI, String appName, Long eventId) {
        var hit = new EndpointHit();
        var date = LocalDateTime.now().toString();
        hit.setApp(appName);
        hit.setEventId(eventId);
        hit.setUri(URI);
        hit.setIp(ip);
        hit.setTimestamp(date);
        return hit;
    }

    private HttpHeaders setHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }
}
