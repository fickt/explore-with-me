package ru.yandex.project.service.participationrequest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.project.service.participationrequest.dto.NewParticipationRequestDto;
import ru.yandex.project.service.participationrequest.dto.ParticipationRequestDto;
import ru.yandex.project.service.participationrequest.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@Slf4j
public class ParticipationController {
    private static final String ENDPOINT_GET_USER_REQUESTS = "/users/{userId}";
    private static final String ENDPOINT_CANCEL_USER_REQUEST = "/{requestId}/users/{userId}/cancel";
    private final ParticipationRequestService requestService;

    @Autowired
    ParticipationController(ParticipationRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ParticipationRequestDto addParticipationRequest(@RequestBody NewParticipationRequestDto requestDto) {
        log.info("POST /requests requestDto: {}", requestDto);
        return requestService.addParticipationRequest(requestDto);
    }

    @GetMapping(ENDPOINT_GET_USER_REQUESTS)
    public List<ParticipationRequestDto> getAllParticipationRequestsOfUser(@PathVariable Long userId) {
        log.info("GET /requests/users/{}", userId);
        return requestService.getAllParticipationRequestsOfUser(userId);
    }

    @PatchMapping(ENDPOINT_CANCEL_USER_REQUEST)
    public ParticipationRequestDto cancelParticipationRequestOfUser(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("GET /requests/{}/users/{}/cancel", requestId, userId);
        return requestService.cancelParticipationRequestOfUser(userId, requestId);
    }
}
