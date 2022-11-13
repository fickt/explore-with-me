package com.example.apigateway.dto.eventdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
public class UpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    @FutureOrPresent(message = "event date should be current or future")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long eventId;
    private Boolean paid;
    private Long participantLimit;
    private String title;
}
