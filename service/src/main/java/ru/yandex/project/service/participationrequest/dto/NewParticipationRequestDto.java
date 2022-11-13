package ru.yandex.project.service.participationrequest.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.project.service.participationrequest.model.ParticipationRequest;
import ru.yandex.project.service.participationrequest.status.RequestStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NewParticipationRequestDto {
    @DateTimeFormat(pattern = "uuuu-MM-dd hh:mm:ss")
    private LocalDateTime created;
    @NotNull(message = "event id should not be empty")
    private Long event;
    private Long requester;

    public ParticipationRequest toEntity() {
        var request = new ParticipationRequest();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(created);
        request.setStatus(RequestStatus.PENDING);
        return request;
    }
}
