package com.example.apigateway.dto.eventdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotBlank(message = "event annotation should not be empty")
    private String annotation;
    @NotBlank(message = "event description should not be empty")
    private String description;
    @NotNull(message = "category should not be empty")
    private Long category;
    @NotNull(message = "Location should not be empty")
    private Location location;
    @NotNull(message = "event date should not be empty")
    @FutureOrPresent(message = "event date should be current or future")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    @NotBlank(message = "event title should not be empty")
    private String title;
}
