package ru.yandex.project.service.rating.dto;

import lombok.Data;
import ru.yandex.project.service.event.dto.EventFullDto;


@Data
public class RatingDto {
    private Long eventId;
    private long likes;
    private long dislikes;
    private EventFullDto event;
}
