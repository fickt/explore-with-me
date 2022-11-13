package ru.yandex.project.service.participationrequest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.project.service.participationrequest.dto.ParticipationRequestDto;
import ru.yandex.project.service.participationrequest.status.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "REQUEST_TABLE")
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CREATED")
    @DateTimeFormat(pattern = "uuuu-MM-dd hh:mm:ss")
    private LocalDateTime created;
    @Column(name = "EVENT_ID")
    private Long event;
    @Column(name = "REQUESTER_ID")
    private Long requester;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public ParticipationRequestDto toDto() {
        var request = new ParticipationRequestDto();
        request.setStatus(status.name());
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(created);
        request.setId(id);
        return request;
    }
}
