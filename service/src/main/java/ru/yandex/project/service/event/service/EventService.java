package ru.yandex.project.service.event.service;


import ru.yandex.project.service.event.dto.*;
import ru.yandex.project.service.participationrequest.dto.ParticipationRequestDto;
import ru.yandex.project.service.statistics.EndpointHit;

import java.util.List;

public interface EventService {

    EventFullDto getEventForUnauthUser(Long eventId, EndpointHit hit);

    EventFullDto addEvent(Long userId, NewEventDto eventDto);

    EventFullDto editEvent(Long userId, UpdateEventRequest eventRequest);

    List<EventShortDto> getEventsOfUser(Long userId);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto cancelEventOfUser(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsOfUserInEvent(Long userId, Long eventId);

    ParticipationRequestDto confirmRequestOfUser(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectRequestOfUser(Long userId, Long eventId, Long reqId);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

    EventFullDto adminEditEvent(Long eventId, AdminUpdateEventRequestDto updateEventRequest);

    List<EventShortDto> getAllEvents(String[] categories, String text, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Long from, Long size);

    List<EventFullDto> getAllEventsAdmin(String[] users, String[] states, String[] categories, String rangeStart, String rangeEnd, Long from, Long size);
}
