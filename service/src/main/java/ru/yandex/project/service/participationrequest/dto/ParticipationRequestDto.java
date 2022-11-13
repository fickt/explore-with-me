package ru.yandex.project.service.participationrequest.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {
    private Long id;
    @DateTimeFormat(pattern = "uuuu-MM-dd hh:mm:ss")
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private String status;
}
