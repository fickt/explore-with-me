package ru.yandex.project.service.rating.service;

import ru.yandex.project.service.rating.dto.RatingDto;

import java.util.List;

public interface RatingService {
    RatingDto likeEvent(Long eventId, Long userId);

    RatingDto dislikeEvent(Long eventId, Long userId);

    List<RatingDto> getSortedEventsWithRating(Long size, String sort);
}
