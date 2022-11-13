package ru.yandex.project.service.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.project.service.category.model.Category;
import ru.yandex.project.service.event.dto.EventFullDto;
import ru.yandex.project.service.event.dto.EventShortDto;
import ru.yandex.project.service.event.location.Location;
import ru.yandex.project.service.event.status.EventStatus;
import ru.yandex.project.service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "EVENT_TABLE")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ANNOTATION")
    private String annotation;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CATEGORY_ID")
    private  Long categoryId;
    @Column(name = "LATITUDE")
    private Float lat;
    @Column(name = "LONGITUDE")
    private Float lon;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID", insertable=false, updatable=false)
    private Category category;
    @Column(name = "EVENT_DATE")
    @DateTimeFormat(pattern = "uuuu-MM-dd hh:mm:ss")
    private LocalDateTime eventDate;
    @Column(name = "INITIATOR_ID")
    private Long initiatorId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "INITIATOR_ID", insertable=false, updatable=false)
    private User initiator;
    @Column(name = "PAID")
    private boolean paid;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
    @Column(name = "CONFIRMED_REQUESTS")
    private int confirmedRequests;
    @Column(name = "PARTICIPANT_LIMIT")
    private int participantLimit;
    @DateTimeFormat(pattern = "uuuu-MM-dd hh:mm:ss")
    private LocalDateTime publishedOn;
    @Column(name = "REQUEST_MODERATION")
    private Boolean requestModeration;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EventStatus state;

    public EventFullDto toFullDto() {
        var eventFullDto = new EventFullDto();
        var location = new Location();
        location.setLon(lon);
        location.setLat(lat);
        eventFullDto.setId(id);
        eventFullDto.setAnnotation(annotation);
        eventFullDto.setCategory(category);
        eventFullDto.setInitiator(initiator.toShortDto());
        eventFullDto.setEventDate(eventDate);
        eventFullDto.setLocation(location);
        eventFullDto.setPaid(paid);
        eventFullDto.setTitle(title);
        eventFullDto.setParticipantLimit(participantLimit);
        eventFullDto.setRequestModeration(requestModeration);
        eventFullDto.setPublishedOn(publishedOn);
        eventFullDto.setDescription(description);
        eventFullDto.setCreatedOn(createdOn);
        eventFullDto.setState(state.name());
        eventFullDto.setConfirmedRequests(confirmedRequests);
        return eventFullDto;
    }

    public EventShortDto toShortDto() {
        var eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(annotation);
        eventShortDto.setEventDate(eventDate);
        eventShortDto.setCategory(category.toDto());
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setInitiator(initiator.toShortDto());
        eventShortDto.setDescription(description);
        eventShortDto.setId(id);
        eventShortDto.setTitle(title);
        eventShortDto.setPaid(paid);
        return eventShortDto;
    }
}
