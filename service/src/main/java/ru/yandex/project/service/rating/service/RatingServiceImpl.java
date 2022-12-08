package ru.yandex.project.service.rating.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.project.service.event.dto.EventFullDto;
import ru.yandex.project.service.event.model.Event;
import ru.yandex.project.service.event.repository.EventRepository;
import ru.yandex.project.service.event.status.EventStatus;
import ru.yandex.project.service.exception.EventIsNotConductedException;
import ru.yandex.project.service.exception.NotFoundException;
import ru.yandex.project.service.exception.NotParticipatorException;
import ru.yandex.project.service.exception.UnavailableEventException;
import ru.yandex.project.service.participationrequest.repository.ParticipationRequestRepository;
import ru.yandex.project.service.participationrequest.status.RequestStatus;
import ru.yandex.project.service.rating.dto.RatingDto;
import ru.yandex.project.service.rating.repository.RatingRepository;
import ru.yandex.project.service.rating.model.Rating;
import ru.yandex.project.service.statistics.StatisticsClient;
import ru.yandex.project.service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.project.service.exception.Message.*;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {

    private static final Long MIN_PAGEABLE_SIZE = 0L;
    private static final String SORT_LIKE = "LIKE";
    private static final String SORT_DISLIKE = "DISLIKE";
    private final RatingRepository ratingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository requestRepository;
    private final StatisticsClient statisticsClient;


    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository,
                             EventRepository eventRepository,
                             UserRepository userRepository,
                             ParticipationRequestRepository requestRepository,
                             StatisticsClient statisticsClient) {
        this.ratingRepository = ratingRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.statisticsClient = statisticsClient;
    }

    @Override
    public RatingDto likeEvent(Long eventId, Long userId) {
        var event = getEventById(eventId);
        isValid(eventId, userId, event);

        //if dislike from this user already exists, it will be replaced with like
        if (ratingRepository.checkDislike(eventId, userId) > 1) {
            ratingRepository.dislikeToLike(eventId, userId);
            return ratingRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException(RATING_NOT_FOUND.get())).toDto();
        }

        // if like already exists, it will be deleted
        if (ratingRepository.checkLike(eventId, userId) > 1) {
            ratingRepository.removeLike(eventId, userId);

            return ratingRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException(RATING_NOT_FOUND.get())).toDto();
        }

        //if it is first like, it creates new record with likes and dislikes
        if (!ratingRepository.existsByEventId(eventId)) {
            ratingRepository.createNewRating(eventId);
        }

        ratingRepository.likeEvent(eventId, userId);
        return ratingRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(RATING_NOT_FOUND.get())).toDto();
    }

    @Override
    public RatingDto dislikeEvent(Long eventId, Long userId) {
        var event = getEventById(eventId);
        isValid(eventId, userId, event);

        //if dislike from this user already exists, it will be deleted
        if (ratingRepository.checkDislike(eventId, userId) > 1) {
            ratingRepository.removeDislike(eventId, userId);
            return ratingRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException(RATING_NOT_FOUND.get())).toDto();
        }
        //if like from this user already exists, it will be replaced with dislike
        if (ratingRepository.checkLike(eventId, userId) > 1) {
            ratingRepository.likeToDislike(eventId, userId);
            return ratingRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException(RATING_NOT_FOUND.get())).toDto();
        }
        //if it is first dislike, it creates new record with likes and dislikes
        if (!ratingRepository.existsByEventId(eventId)) {
            ratingRepository.createNewRating(eventId);
        }

        ratingRepository.dislikeEvent(eventId, userId);
        return ratingRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(RATING_NOT_FOUND.get())).toDto();
    }

    @Override
    public List<RatingDto> getSortedEventsWithRating(Long size, String sort) {
        switch (sort) {
            case SORT_LIKE:
                return ratingRepository.findAllByOrderByLikesDesc(PageRequest.of(MIN_PAGEABLE_SIZE.intValue(),
                        size.intValue())).stream()
                        .map(Rating::toDto)
                        .collect(Collectors.toUnmodifiableList());
            case SORT_DISLIKE:
                return ratingRepository.findAllByOrderByDislikesDesc(PageRequest.of(MIN_PAGEABLE_SIZE.intValue(),
                        size.intValue())).stream()
                        .map(Rating::toDto)
                        .collect(Collectors.toUnmodifiableList());
            default:
                throw new UnsupportedOperationException(String.format(UNKNOWN_SORT_TYPE.get(), sort));
        }
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById((eventId))
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.get(), eventId)));
    }

    private void checkUserExists(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.get(), userId)));
        log.info(String.format("User with id=%s was successfully found!", userId));
    }

    /**
     * Method for validating {@link Event}
     * and checking if {@link ru.yandex.project.service.participationrequest.model.ParticipationRequest} exists
     */
    private void isValid(Long eventId, Long userId, Event event) {
        checkUserExists(userId);
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new UnavailableEventException();
        }
        // if event has not been conducted yet, then can not be liked/disliked
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new EventIsNotConductedException(String.format(EVENT_NOT_CONDUCTED.get(), eventId));
        }

        // Only users participated in event can like/dislike event
        requestRepository.findByRequesterAndEventAndStatus(userId, eventId, RequestStatus.CONFIRMED)
                .orElseThrow(() -> new NotParticipatorException(String.format(NOT_PARTICIPATOR.get(), userId)));
    }

    private EventFullDto attachViews(EventFullDto event) {
        event.setViews(statisticsClient.getViews(event.getId()));
        return event;
    }
}
