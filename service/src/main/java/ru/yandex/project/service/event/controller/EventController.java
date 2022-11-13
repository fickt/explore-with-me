package ru.yandex.project.service.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.project.service.event.dto.*;
import ru.yandex.project.service.event.service.EventService;
import ru.yandex.project.service.participationrequest.dto.ParticipationRequestDto;
import ru.yandex.project.service.statistics.EndpointHit;

import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
public class EventController {
    private static final String ENDPOINT_GET_EVENTS_ADMIN = "/admin";
    private static final String ENDPOINT_GET_EVENT_BY_ID = "/{eventId}/users/{userId}";
    private static final String ENDPOINT_CANCEL_EVENT = "{eventId}/users/{userId}/cancel";
    private static final String ENDPOINT_USER_ID = "/users/{userId}";
    private static final String ENDPOINT_EVENT_ID = "/{eventId}";
    private static final String ENDPOINT_GET_USER_REQUESTS_IN_EVENT = "/{eventId}/users/{userId}/requests";
    private static final String ENDPOINT_REJECT_REQUEST = "/{eventId}/users/{userId}/requests/{reqId}/reject";
    private static final String ENDPOINT_CONFIRM_REQUEST = "/{eventId}/users/{userId}/requests/{reqId}/confirm";
    private static final String ENDPOINT_PUBLISH_EVENT = "/{eventId}/publish";
    private static final String ENDPOINT_REJECT_EVENT = "/{eventId}/reject";

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getAllEvents(@RequestParam(value = "text", required = false) String text,
                                            @RequestParam(value = "paid", required = false) Boolean paid,
                                            @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                            @RequestParam(value = "onlyAvailable", required = false) Boolean onlyAvailable,
                                            @RequestParam(value = "sort", required = false) String sort,
                                            @RequestParam(value = "from", required = false) Long from,
                                            @RequestParam(value = "size", required = false) Long size,
                                            @RequestParam(value = "categories", required = false) String[] categories) {
        log.info("GET /events");
        return eventService.getAllEvents(categories, text, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping(ENDPOINT_GET_EVENTS_ADMIN)
    public List<EventFullDto> getAllEventsAdmin(@RequestParam(value = "users", required = false) String[] users,
                                                 @RequestParam(value = "states", required = false) String[] states,
                                                 @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                                 @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                                 @RequestParam(value = "from", required = false) Long from,
                                                 @RequestParam(value = "size", required = false) Long size,
                                                 @RequestParam(value = "categories", required = false) String[] categories) {
        log.info("GET events/admin");
        return eventService.getAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }


    @GetMapping(ENDPOINT_GET_EVENT_BY_ID)
    public EventFullDto getEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET /events/{}/users/{}", eventId, userId);
        return eventService.getEventById(userId, eventId);
    }

    @PostMapping(ENDPOINT_USER_ID)
    public EventFullDto addEvent(@PathVariable Long userId, @RequestBody NewEventDto eventDto) {
        log.info("POST /events/users/{}, eventDto: {}", userId, eventDto);
        return eventService.addEvent(userId, eventDto);
    }

    @PatchMapping(ENDPOINT_USER_ID)
    public EventFullDto editEvent(@PathVariable Long userId, @RequestBody UpdateEventRequest eventRequest) {
        log.info("PATCH /events/users/{}, eventRequest: {}", userId, eventRequest);
        return eventService.editEvent(userId, eventRequest);
    }

    @GetMapping(ENDPOINT_USER_ID)
    public List<EventShortDto> getEventsOfUser(@PathVariable Long userId) {
        log.info("GET events/users/{}", userId);
        return eventService.getEventsOfUser(userId);
    }

    @PatchMapping(ENDPOINT_CANCEL_EVENT)
    public EventFullDto cancelEventOfUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("PATCH events/{}/users/{}", eventId, userId);
        return eventService.cancelEventOfUser(userId, eventId);
    }

    @GetMapping(ENDPOINT_GET_USER_REQUESTS_IN_EVENT)
    public List<ParticipationRequestDto> getRequestsOfUserInEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET events/{}/users/{}/requests", eventId, userId);
        return eventService.getRequestsOfUserInEvent(userId, eventId);
    }

    @PatchMapping(ENDPOINT_CONFIRM_REQUEST)
    public ParticipationRequestDto confirmRequestOfUser(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long reqId) {
        log.info("PATCH events/{}/users/{}/requests/{}/confirm", eventId, userId,reqId);
        return eventService.confirmRequestOfUser(userId, eventId, reqId);
    }

    @PatchMapping(ENDPOINT_REJECT_REQUEST)
    public ParticipationRequestDto rejectRequestOfUser(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long reqId) {
        log.info("PATCH events/{}/users/{}/requests/{}/reject", eventId, userId, reqId);
        return eventService.rejectRequestOfUser(userId, eventId, reqId);
    }

    @PatchMapping(ENDPOINT_PUBLISH_EVENT)
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        log.info("PATCH /events/{}/publish", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping(ENDPOINT_REJECT_EVENT)
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        log.info("PATCH /events/{}/reject", eventId);
        return eventService.rejectEvent(eventId);
    }

    @PutMapping(ENDPOINT_EVENT_ID)
    public EventFullDto adminEditEvent(@PathVariable Long eventId, @RequestBody AdminUpdateEventRequestDto updateEventRequest) {
        log.info("PUT admin /events/{} updateEventRequest: {}", eventId, updateEventRequest);
        return eventService.adminEditEvent(eventId, updateEventRequest);
    }

    /**
     * Endpoint is POST as it has to receive RequestBody for statistics service
     * @param hit - info for statistics
     */
    @PostMapping(ENDPOINT_EVENT_ID)
    public EventFullDto getEventById(@PathVariable Long eventId, @RequestBody EndpointHit hit) {
        log.info("POST /events/{} EndpointHit: {}", eventId, hit);
        return eventService.getEventForUnauthUser(eventId, hit);
    }
}
