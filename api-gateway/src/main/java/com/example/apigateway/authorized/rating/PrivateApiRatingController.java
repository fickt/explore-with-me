package com.example.apigateway.authorized.rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/ratings")
public class PrivateApiRatingController {
    private static final String ENDPOINT_LIKE_EVENT = "/users/{userId}/events/{eventId}/like";
    private static final String ENDPOINT_DISLIKE_EVENT = "/users/{userId}/events/{eventId}/dislike";
    private final PrivateApiRatingClient client;

    @Autowired
    public PrivateApiRatingController(PrivateApiRatingClient client) {
        this.client = client;
    }

    @PutMapping(ENDPOINT_LIKE_EVENT)
    public ResponseEntity<Object> likeEvent(@PositiveOrZero @PathVariable Long userId,
                                            @PositiveOrZero @PathVariable Long eventId) {
        log.info("PUT /ratings/users/{}/events/{}/like", userId, eventId);
        return client.likeEvent(userId, eventId);
    }

    @PutMapping(ENDPOINT_DISLIKE_EVENT)
    public ResponseEntity<Object> dislikeEvent(@PositiveOrZero @PathVariable Long userId,
                                            @PositiveOrZero @PathVariable Long eventId) {
        log.info("PUT /ratings/users/{}/events/{}/dislike", userId, eventId);
        return client.dislikeEvent(userId, eventId);
    }
}
