package com.example.apigateway.admin.user.client;

import com.example.apigateway.dto.userdto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdminApiUserClient {

    private static final String API_PREFIX = "/users";
    private static final String ENDPOINT_BLANK = "";
    private static final String ENDPOINT_GET_USERS_WITH_REQUEST_PARAMS = "?from={from}&size={size}&ids={ids}";
    private final RestTemplate restTemplate;

    @Autowired
    public AdminApiUserClient(@Value("${explore-with-me-service.url}") String serviceAddress, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serviceAddress + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> addUser(UserDto userDto) {
        try {
            return restTemplate.exchange(ENDPOINT_BLANK, HttpMethod.POST, new HttpEntity<>(userDto, setHeaders()), Object.class);
        } catch (
                HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public ResponseEntity<Object> getUsers(Long from, Long size, Integer[] ids) {
        List<Integer> idsAsList = Collections.emptyList();
        if (ids != null) {
            idsAsList = Arrays.stream(ids)
                    .collect(Collectors.toList());
        }

        var params = Map.of(
                "from", from,
                "size", size,
                "ids", idsAsList
        );

        try {
            return restTemplate.exchange(ENDPOINT_GET_USERS_WITH_REQUEST_PARAMS, HttpMethod.GET, null, Object.class, params);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
    }

    public void deleteUser(Long userId) {
        restTemplate.delete("/" + userId);
    }

    private HttpHeaders setHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }
}
