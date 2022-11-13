package com.example.apigateway.admin.event.controller;


import com.example.apigateway.admin.event.client.AdminApiEventClient;
import com.example.apigateway.dto.AdminUpdateEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/events")
public class AdminApiEventController {
    private static final String ENDPOINT_PUBLISH_EVENT = "/{eventId}/publish";
    private static final String ENDPOINT_REJECT_EVENT = "/{eventId}/reject";
    private static final String ENDPOINT_EDIT_EVENT = "/{eventId}";
    private final AdminApiEventClient client;

    @Autowired
    public AdminApiEventController(AdminApiEventClient client) {
        this.client = client;
    }

    @GetMapping
    public ResponseEntity<Object> getEvents(@RequestParam(value = "users", required = false) List<Integer> users,
                                            @RequestParam(value = "states", required = false) List<String> states,
                                            @RequestParam(value = "categories", required = false) List<Integer> categories,
                                            @RequestParam(value = "rangeStart") String rangeStart,
                                            @RequestParam(value = "rangeEnd") String rangeEnd,
                                            @PositiveOrZero @RequestParam(value = "from", defaultValue = "1") Long from,
                                            @Positive @RequestParam(value = "size", defaultValue = "10") Long size) {
        return client.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(ENDPOINT_PUBLISH_EVENT)
    public ResponseEntity<Object> publishEvent(@PathVariable Long eventId) {
        return client.publishEvent(eventId);
    }

    @PatchMapping(ENDPOINT_REJECT_EVENT)
    public ResponseEntity<Object> rejectEvent(@PathVariable Long eventId) {
        return client.rejectEvent(eventId);
    }

    @PutMapping(ENDPOINT_EDIT_EVENT)
    public ResponseEntity<Object> editEvent(@PathVariable Long eventId,
                                            @RequestBody AdminUpdateEventRequest updateRequest) {
        return client.editRequest(eventId, updateRequest);
    }

}
