package com.example.apigateway.authorized.event.controller;

import com.example.apigateway.authorized.event.client.PrivateApiEventClient;
import com.example.apigateway.dto.eventdto.NewEventDto;
import com.example.apigateway.dto.eventdto.UpdateEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/events")
public class PrivateApiEventController {
    private static final String ENDPOINT_GET_EVENT_BY_ID = "/{eventId}";
    private static final String ENDPOINT_CANCEL_EVENT = "/{eventId}";
    private static final String ENDPOINT_GET_USER_REQUESTS_IN_EVENT = "/{eventId}/requests";
    private static final String ENDPOINT_CONFIRM_REQUEST = "/{eventId}/requests/{reqId}/confirm";
    private static final String ENDPOINT_REJECT_REQUEST = "/{eventId}/requests/{reqId}/reject";
    private final PrivateApiEventClient client;

    @Autowired
    public PrivateApiEventController(PrivateApiEventClient client) {
        this.client = client;
    }

    @GetMapping(ENDPOINT_GET_EVENT_BY_ID)
    public ResponseEntity<Object> getEventById(@PositiveOrZero @PathVariable Long userId,
                                           @PositiveOrZero @PathVariable Long eventId) {
        log.info(String.format("GET users/%s/events/%s", userId, eventId));
        return client.getEventById(userId, eventId);
    }

    @PostMapping
    public ResponseEntity<Object> addEvent(@PositiveOrZero @PathVariable Long userId, @Valid @RequestBody NewEventDto eventDto) {
        log.info(String.format("POST users/%s/events, body=%s", userId, eventDto));
        return client.addEvent(userId, eventDto);
    }

    @PatchMapping
    public ResponseEntity<Object> editEvent(@PositiveOrZero @PathVariable Long userId, @Valid @RequestBody UpdateEventRequest eventRequest) {
        log.info(String.format("PATCH users/%s/events, body=%s", userId, eventRequest));
        return client.editEvent(userId, eventRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getEventsOfUser(@PositiveOrZero @PathVariable Long userId) {
        log.info(String.format("GET users/%s/events", userId));
        return client.getEventsOfUser(userId);
    }

    @PatchMapping(ENDPOINT_CANCEL_EVENT)
    public ResponseEntity<Object> cancelEventOfUser(@PositiveOrZero @PathVariable Long userId,
                                                    @PositiveOrZero @PathVariable Long eventId) {
        log.info(String.format("PATCH users/%s/events/%s", userId, eventId));
        return client.cancelEventOfUser(userId, eventId);
    }

    @GetMapping(ENDPOINT_GET_USER_REQUESTS_IN_EVENT)
    public ResponseEntity<Object> getRequestsOfUserInEvent(@PositiveOrZero @PathVariable Long userId,
                                                           @PositiveOrZero @PathVariable Long eventId) {
        log.info("GET users/{}/events/{}/requests", userId, eventId); ///users/{userId}/events
        return client.getRequestsOfUserInEvent(userId, eventId);
    }

    @PatchMapping(ENDPOINT_CONFIRM_REQUEST)
    public ResponseEntity<Object> confirmRequestOfUser(@PositiveOrZero @PathVariable Long userId,
                                                       @PositiveOrZero @PathVariable Long eventId,
                                                       @PositiveOrZero @PathVariable Long reqId) {
        log.info(String.format("PATCH users/%s/events/%s/requests/%s/confirm", userId, eventId, reqId));
        return client.confirmRequestOfUser(userId, eventId, reqId);
    }

    @PatchMapping(ENDPOINT_REJECT_REQUEST)
    public ResponseEntity<Object> rejectRequestOfUser(@PositiveOrZero @PathVariable Long userId,
                                                      @PositiveOrZero @PathVariable Long eventId,
                                                      @PositiveOrZero @PathVariable Long reqId) {
        log.info(String.format("PATCH users/%s/events/%s/requests/%s/reject", userId, eventId, reqId));
        return client.rejectRequestOfUser(userId, eventId, reqId);
    }


}
