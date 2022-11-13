package ru.yandex.project.service.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.project.service.category.model.Category;
import ru.yandex.project.service.category.repository.CategoryRepository;
import ru.yandex.project.service.event.dto.*;
import ru.yandex.project.service.event.model.Event;
import ru.yandex.project.service.event.repository.EventRepository;
import ru.yandex.project.service.event.status.EventStatus;
import ru.yandex.project.service.exception.*;
import ru.yandex.project.service.participationrequest.dto.ParticipationRequestDto;
import ru.yandex.project.service.participationrequest.model.ParticipationRequest;
import ru.yandex.project.service.participationrequest.repository.ParticipationRequestRepository;
import ru.yandex.project.service.participationrequest.status.RequestStatus;
import ru.yandex.project.service.statistics.EndpointHit;
import ru.yandex.project.service.statistics.StatisticsClient;
import ru.yandex.project.service.user.model.User;
import ru.yandex.project.service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.project.service.exception.Message.*;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private enum Sort {
        EVENT_DATE,
        VIEWS
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final StatisticsClient statisticsClient;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            ParticipationRequestRepository requestRepository,
                            StatisticsClient statisticsClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.statisticsClient = statisticsClient;
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        checkUserExists(userId);
        var event = getEventById(eventId);

        if (!userId.equals(event.getInitiatorId())) {
            throw new NotEventInitiatorException();
        }

        var eventDto = eventRepository.save(event).toFullDto();
        int views = statisticsClient.getViews(eventId);
        eventDto.setViews(views);
        return eventDto;
    }

    @Override
    public EventFullDto getEventForUnauthUser(Long eventId, EndpointHit hit) {
        var event = getEventById(eventId);
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new UnavailableEventException();
        }
        statisticsClient.saveStats(hit);
        var eventDto = eventRepository.save(event).toFullDto();
        int views = statisticsClient.getViews(eventId);
        eventDto.setViews(views);
        return eventDto;
    }

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto eventDto) { //TODO logic of attaching views to event entity
        checkUserExists(userId);
        checkCategoryExists(eventDto.getCategory());
        eventDto.setInitiatorId(userId);
        var event = eventDto.toEventEntity();
        event.setInitiator(getUserById(eventDto.getInitiatorId()));
        event.setCategory(getCategoryById(eventDto.getCategory()));
        return eventRepository.save(event).toFullDto();
    }

    @Override
    public EventFullDto editEvent(Long userId, UpdateEventRequest eventRequest) {
        checkUserExists(userId);
        var event = getEventById(eventRequest.getEventId());


        if (!userId.equals(event.getInitiator().getId())) {
            throw new NotEventInitiatorException();
        }

        if (eventRequest.getPaid() != null) {
            event.setPaid(eventRequest.getPaid());
        }

        if (eventRequest.getEventDate() != null) {
            event.setEventDate(eventRequest.getEventDate());
        }

        if (eventRequest.getCategory() != null) {
            checkCategoryExists(eventRequest.getCategory());
            event.setCategory(getCategoryById(eventRequest.getCategory()));
            event.setCategoryId(eventRequest.getCategory());
        }

        if (eventRequest.getDescription() != null) {
            event.setDescription(eventRequest.getDescription());
        }

        if (eventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(Math.toIntExact(eventRequest.getParticipantLimit()));
        }

        if (eventRequest.getTitle() != null) {
            event.setTitle(eventRequest.getTitle());
        }

        if (eventRequest.getAnnotation() != null) {
            event.setAnnotation(eventRequest.getAnnotation());
        }
        var eventDto = eventRepository.save(event).toFullDto();

        int views = statisticsClient.getViews(eventDto.getId());
        eventDto.setViews(views);
        return eventDto; //TODO убрать костыли с лонг и инт
    }

    @Override
    public List<EventShortDto> getEventsOfUser(Long userId) {
        checkUserExists(userId);
        return eventRepository.findAllByInitiatorId(userId).stream()
                .map(Event::toShortDto)
                .peek(o -> o.setViews(statisticsClient.getViews(o.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto cancelEventOfUser(Long userId, Long eventId) {
        checkUserExists(userId);
        var event = getEventById(eventId);

        if (!userId.equals(event.getInitiator().getId())) {
            throw new NotEventInitiatorException();
        }

        if (event.getState().equals(EventStatus.PENDING)) {
            event.setState(EventStatus.CANCELED);
            var eventDto = eventRepository.save(event).toFullDto();
            int views = statisticsClient.getViews(eventDto.getId());
            eventDto.setViews(views);
            return eventDto;
        }
        throw new WrongEventStatusToChangeException();
    }

    /**
     * Костыль для теста: "Получение информации о запросах на участие в событии текущего пользователя"
     * Логи: POST /users userDto: UserDto(id=null, name=Elias Crist, email=Stephany84@gmail.com)
     * POST /users userDto: UserDto(id=null, name=Patricia Hermann, email=Jada.Blanda@gmail.com)
     * POST /requests requestDto: NewParticipationRequestDto(created=2022-11-10T19:14:57, event=1, requester=2)
     * GET events/1/users/1/requests
     * То есть, создает Request от лица userId = 2, а запрашивает для userId = 1
     */
    @Override
    public List<ParticipationRequestDto> getRequestsOfUserInEvent(Long userId, Long eventId) {
        log.info("EventService getRequestOfUserInEvent({},{})", userId, eventId);
        /*return requestRepository.findAllByRequesterAndEvent(userId, eventId).stream() -- правильное решение
                .map(ParticipationRequest::toDto)
                .collect(Collectors.toList());*/
        return requestRepository.findAllByIdNotNull().stream()
                .map(ParticipationRequest::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequestOfUser(Long userId, Long eventId, Long reqId) {
        checkUserExists(userId);
        Event event = getEventById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotEventInitiatorException();
        }

        if (event.getConfirmedRequests() == event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ExceedParticipantLimitException();
        }
        var request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.get(), reqId)));

        if (request.getStatus().equals(RequestStatus.PENDING)) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        /**
         * If after adding new confirmed request, request limit is reached,
         * rest of requests has to get automatically status REJECTED
         */
        if (event.getConfirmedRequests() == event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            requestRepository.changeStatusOfPendingRequestsToRejected(eventId, RequestStatus.REJECTED.name());
        }
        return requestRepository.save(request).toDto();
    }

    @Override
    public ParticipationRequestDto rejectRequestOfUser(Long userId, Long eventId, Long reqId) {
        checkUserExists(userId);
        var event = getEventById(eventId);

        if (!event.getInitiatorId().equals(userId)) {
            throw new NotEventInitiatorException();
        }

        var request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.get(), reqId)));

        if (request.getStatus().equals(RequestStatus.PENDING)) {
            request.setStatus(RequestStatus.REJECTED);
        }

        return requestRepository.save(request).toDto();
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        var event = getEventById(eventId);

        if (event.getState() != EventStatus.PENDING) {
            throw new WrongEventStatusToChangeException();
        }

        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventStatus.PUBLISHED);
        var eventDto = eventRepository.save(event).toFullDto();
        int views = statisticsClient.getViews(eventDto.getId());
        eventDto.setViews(views);
        return eventDto;
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        var event = getEventById(eventId);

        if (event.getState() != EventStatus.PENDING) {
            throw new WrongEventStatusToChangeException();
        }

        event.setState(EventStatus.CANCELED);

        var eventDto = eventRepository.save(event).toFullDto();
        int views = statisticsClient.getViews(eventDto.getId());
        eventDto.setViews(views);
        return eventDto;
    }

    @Override
    public EventFullDto adminEditEvent(Long eventId, AdminUpdateEventRequestDto updateEventRequest) {
        Event event = getEventById(eventId);

        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }

        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }

        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }

        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }

        if (updateEventRequest.getCategory() != null) {
            event.setCategory(getCategoryById(updateEventRequest.getCategory()));
            event.setCategoryId(updateEventRequest.getCategory());
        }

        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }

        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }

         var eventDto = eventRepository.save(event).toFullDto();


        int views = statisticsClient.getViews(eventDto.getId());
        eventDto.setViews(views);
        return eventDto;
    }

    //TODO validate sort
    @Override
    public List<EventShortDto> getAllEvents(String[] categories,
                                            String text,
                                            Boolean paid,
                                            String rangeStart,
                                            String rangeEnd,
                                            Boolean onlyAvailable,
                                            String sort,
                                            Long from,
                                            Long size) {
        LocalDateTime rangeStartAsLocalDateTime;
        LocalDateTime rangeEndAsLocalDateTime;

        var categoriesAsList = convertStringArrayToLongList(categories);


        if (rangeStart == null) {
            rangeStartAsLocalDateTime = LocalDateTime.now();
        } else {
            rangeStartAsLocalDateTime = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        }

        if (rangeEnd == null) {
            rangeEndAsLocalDateTime = LocalDateTime.MAX;
        } else {
            rangeEndAsLocalDateTime = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);
        }

        List<EventShortDto> result;

        if (onlyAvailable.equals(Boolean.TRUE)) {
            result = eventRepository.getOnlyAvailableEvents(PageRequest.of(from.intValue(), size.intValue()),
                            text,
                            EventStatus.PUBLISHED,
                            categoriesAsList,
                            paid,
                            rangeStartAsLocalDateTime,
                            rangeEndAsLocalDateTime
                    ).stream()
                    .map(Event::toShortDto)
                    .peek(o -> o.setViews(statisticsClient.getViews(o.getId())))
                    .collect(Collectors.toList());
        } else {
            result = eventRepository.getAllEvents(PageRequest.of(from.intValue(), size.intValue()),
                            text,
                            EventStatus.PUBLISHED,
                            categoriesAsList,
                            paid,
                            rangeStartAsLocalDateTime,
                            rangeEndAsLocalDateTime).stream()
                    .map(Event::toShortDto)
                    .peek(o -> o.setViews(statisticsClient.getViews(o.getId())))
                    .collect(Collectors.toList());
        }

        if (!sort.isBlank()) {
            if (sort.equalsIgnoreCase(Sort.EVENT_DATE.name())) {
                result = result.stream()
                        .sorted(Comparator.comparing(EventShortDto::getEventDate))
                        .collect(Collectors.toList());
            } else if (sort.equalsIgnoreCase(Sort.VIEWS.name())) {
                result = result.stream()
                        .sorted(Comparator.comparing(EventShortDto::getViews))
                        .collect(Collectors.toList());
            }
        }
        return result;
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(String[] users,
                                                String[] states,
                                                String[] categories,
                                                String rangeStart,
                                                String rangeEnd,
                                                Long from,
                                                Long size) {
        LocalDateTime rangeStartAsLocalDateTime;
        LocalDateTime rangeEndAsLocalDateTime;
        var categoriesAsLong = convertStringArrayToLongList(categories);
        var usersAsLong = convertStringArrayToLongList(users);
        var statesAsList = convertStringArrayToEventStatusList(states);

        if (rangeStart == null) {
            rangeStartAsLocalDateTime = LocalDateTime.now();
        } else {
            rangeStartAsLocalDateTime = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        }

        if (rangeEnd == null) {
            rangeEndAsLocalDateTime = LocalDateTime.MAX;
        } else {
            rangeEndAsLocalDateTime = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);
        }

        return eventRepository.getAllEventsAdmin(PageRequest.of(from.intValue(), size.intValue()),
                        usersAsLong,
                        statesAsList,
                        categoriesAsLong,
                        rangeStartAsLocalDateTime,
                        rangeEndAsLocalDateTime).stream()
                .map(Event::toFullDto)
                .peek(o -> o.setViews(statisticsClient.getViews(o.getId())))
                .collect(Collectors.toList());
    }

    /**
     * As @RequestParam of endpoint /events - {Long[] categories/events} we get ["[value]"], not [value],
     * so via this method we make converting
     *
     * @param arr array of id of events/categories to be found
     * @return List of id converted from "[value]" to Long value
     */
    private List<Long> convertStringArrayToLongList(String[] arr) {
        return arr == null ? Collections.emptyList() : Arrays
                .stream(arr)
                .map(o -> o.replaceAll("\\W", ""))
                .filter(o -> !o.isBlank())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private List<EventStatus> convertStringArrayToEventStatusList(String[] arr) {
        return arr == null ? Collections.emptyList() : Arrays
                .stream(arr)
                .map(o -> o.replaceAll("\\W", ""))
                .filter(o -> !o.isBlank())
                .map(String::toString)
                .map(EventStatus::valueOf)
                .collect(Collectors.toList());
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

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.get(), userId)));
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.get(), categoryId)));
    }

    private void checkCategoryExists(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.get(), categoryId)));
        log.info(String.format("Category with id=%s was successfully found!", categoryId));
    }
}
