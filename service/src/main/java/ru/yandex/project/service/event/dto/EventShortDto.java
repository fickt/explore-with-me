package ru.yandex.project.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.project.service.category.dto.CategoryDto;
import ru.yandex.project.service.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventShortDto {
    private Long id;
    private UserShortDto initiator;
    private int confirmedRequests;
    private String annotation;
    private String description;
    private CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private boolean paid;
    private String title;
    private int views;
}
