package ru.yandex.project.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.project.service.event.location.Location;
import ru.yandex.project.service.event.model.Event;
import ru.yandex.project.service.event.status.EventStatus;

import java.time.LocalDateTime;

@Data
public class NewEventDto {
    private Long initiatorId;
    private String annotation;
    private String description;
    private Location location;
    private Long category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;

    public Event toEventEntity() {
        var event = new Event();
        event.setAnnotation(annotation);
        event.setDescription(description);
        event.setCategoryId(category);
        event.setEventDate(eventDate);
        event.setLat(location.getLat());
        event.setLon(location.getLon());
        event.setPaid(paid);
        event.setParticipantLimit(participantLimit);
        event.setTitle(title);
        event.setCreatedOn(createdOn);
        event.setRequestModeration(requestModeration);
        event.setInitiatorId(initiatorId);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventStatus.PENDING);
        return event;
    }
}
