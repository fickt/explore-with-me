package ru.yandex.project.service.rating.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.project.service.rating.model.Rating;
import ru.yandex.project.service.rating.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@Slf4j
public class RatingController {
    private static final String ENDPOINT_LIKE_EVENT = "/users/{userId}/events/{eventId}/like";
    private static final String ENDPOINT_DISLIKE_EVENT = "/users/{userId}/events/{eventId}/dislike";

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PutMapping(ENDPOINT_LIKE_EVENT)
    public Rating likeEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        log.info("PUT ratings/users/{userId}/events/{eventId}/like");
        return ratingService.likeEvent(eventId, userId);
    }

    @PutMapping(ENDPOINT_DISLIKE_EVENT)
    public Rating dislikeEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        log.info("PUT ratings/users/{userId}/events/{eventId}/dislike");
        return ratingService.dislikeEvent(eventId, userId);
    }

    @GetMapping
    public List<Rating> getSortedEventsWithRating(@RequestParam("sort") String sort,
                                                  @RequestParam("size") Long size) {
        log.info("GET /ratings?sort={}&size={}", sort, size);
        return ratingService.getSortedEventsWithRating(size, sort);
    }
}
