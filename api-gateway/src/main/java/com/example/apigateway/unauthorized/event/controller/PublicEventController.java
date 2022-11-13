package com.example.apigateway.unauthorized.event.controller;

import com.example.apigateway.unauthorized.event.client.PublicApiEventClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
public class PublicEventController {
    private static final String ENDPOINT_GET_EVENT_BY_ID = "/{eventId}";
    private final PublicApiEventClient client;

    @Autowired
    public PublicEventController(PublicApiEventClient client) {
        this.client = client;
    }

    @GetMapping
    public ResponseEntity<Object> getEvents(@RequestParam(value = "text", required = false) String text,
                                            @RequestParam(value = "paid", required = false) Boolean paid,
                                            @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                            @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(value = "sort", required = false) String sort,
                                            @PositiveOrZero @RequestParam(value = "from", defaultValue = "1") Long from,
                                            @Positive @RequestParam(value = "size", defaultValue = "10") Long size,
                                            @RequestParam(value = "categories", required = false) List<Integer> categories) {
        return client.getEvents(categories, text, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping(ENDPOINT_GET_EVENT_BY_ID)
    public ResponseEntity<Object> getEventById(@PathVariable Long eventId, HttpServletRequest request) {

        return client.getEventById(eventId, request.getRemoteAddr(), request.getRequestURI());
    }
}
