package ru.yandex.project.service.rating.service;

import ru.yandex.project.service.rating.model.Rating;

import java.util.List;

public interface RatingService {
    Rating likeEvent(Long eventId, Long userId);

    Rating dislikeEvent(Long eventId, Long userId);

    List<Rating> getSortedEventsWithRating(Long size, String sort);
}
