package ru.yandex.project.service.participationrequest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.project.service.event.repository.EventRepository;
import ru.yandex.project.service.exception.NotFoundException;
import ru.yandex.project.service.exception.NotRequesterException;
import ru.yandex.project.service.participationrequest.dto.NewParticipationRequestDto;
import ru.yandex.project.service.participationrequest.dto.ParticipationRequestDto;
import ru.yandex.project.service.participationrequest.model.ParticipationRequest;
import ru.yandex.project.service.participationrequest.repository.ParticipationRequestRepository;
import ru.yandex.project.service.participationrequest.status.RequestStatus;
import ru.yandex.project.service.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.project.service.exception.Message.*;

@Service
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Autowired
    public ParticipationRequestServiceImpl (ParticipationRequestRepository requestRepository,
                                            UserRepository userRepository,
                                            EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public ParticipationRequestDto addParticipationRequest(NewParticipationRequestDto requestDto) {

        checkUserExists(requestDto.getRequester());
        checkEventExists(requestDto.getEvent());
        return requestRepository.save(requestDto.toEntity()).toDto();
    }

    @Override
    public List<ParticipationRequestDto> getAllParticipationRequestsOfUser(Long userId) {
        checkUserExists(userId);
        return requestRepository.findAllByRequester(userId).stream()
                .map(ParticipationRequest::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequestOfUser(Long userId, Long requestId) {
        checkUserExists(userId);

        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format(REQUEST_NOT_FOUND.get(), requestId)));
        if (!userId.equals(request.getRequester())) {
            throw new NotRequesterException(NOT_REQUEST_OWNER.get());
        }

        request.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(request).toDto();
    }

    private void checkUserExists(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND.get(), userId)));
    }

    private void checkEventExists(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.get(), eventId)));
    }
}
