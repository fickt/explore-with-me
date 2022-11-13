package ru.yandex.project.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long eventId;
    private Boolean paid;
    private Long participantLimit;
    private String title;
}
