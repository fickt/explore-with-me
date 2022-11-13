package com.example.apigateway.authorized.participationrequest.controller;

import com.example.apigateway.authorized.participationrequest.client.PrivateApiRequestClient;
import com.example.apigateway.dto.participationrequestdto.NewParticipationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/requests")
public class PrivateParticipationRequestController {
    private static final String ENDPOINT_CANCEL_REQUEST = "/{requestId}/cancel";
    private final PrivateApiRequestClient client;

    @Autowired
    public PrivateParticipationRequestController(PrivateApiRequestClient client) {
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<Object> addParticipationRequest(@PositiveOrZero @PathVariable Long userId,
                                                          @RequestParam(value = "eventId") Long eventId) {
        log.info("POST /users/{}/requests?eventId={}",userId, eventId);
        return client.addParticipationRequest(userId, eventId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllParticipationRequestsOfUser(@PositiveOrZero @PathVariable Long userId) {
        return client.getAllParticipationRequestsOfUser(userId);
    }

    /**
     * User can cancel only his own request
     */
    @PatchMapping(ENDPOINT_CANCEL_REQUEST)
    public ResponseEntity<Object> cancelParticipationRequestOfUser(@PositiveOrZero @PathVariable Long userId,
                                                                   @PositiveOrZero @PathVariable Long requestId) {
        return client.cancelParticipationRequestOfUser(userId, requestId);
    }

}
