package ru.yandex.project.service.participationrequest.service;

import ru.yandex.project.service.participationrequest.dto.NewParticipationRequestDto;
import ru.yandex.project.service.participationrequest.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto addParticipationRequest(NewParticipationRequestDto requestDto);

    List<ParticipationRequestDto> getAllParticipationRequestsOfUser(Long userId);

    ParticipationRequestDto cancelParticipationRequestOfUser(Long userId, Long requestId);
}
