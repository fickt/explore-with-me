package com.example.apigateway.admin.compilation.controller;

import com.example.apigateway.admin.compilation.client.AdminApiCompilationClient;
import com.example.apigateway.dto.compilationdto.NewCompilationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/compilations")
public class CompilationController {
    private static final String ENDPOINT_DELETE_COMPILATION = "/{compId}";
    private static final String ENDPOINT_DELETE_EVENT_FROM_COMPILATION = "/{compId}/events/{eventId}";
    private static final String ENDPOINT_ADD_EVENT_TO_COMPILATION = "/{compId}/events/{eventId}";
    private static final String ENDPOINT_PIN_COMPILATION = "/{compId}/pin";
    private static final String ENDPOINT_UNPIN_COMPILATION = "/{compId}/pin";

    private final AdminApiCompilationClient client;

    @Autowired
    public CompilationController(AdminApiCompilationClient client) {
        this.client = client;
    }


    @PostMapping
    public ResponseEntity<Object> addCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {
        log.info("POST /admin/compilations compilationDto: {}", compilationDto);
        return client.addCompilation(compilationDto);
    }

    @DeleteMapping(ENDPOINT_DELETE_COMPILATION)
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compId) {
        return client.deleteCompilation(compId);
    }

    @DeleteMapping(ENDPOINT_DELETE_EVENT_FROM_COMPILATION)
    public ResponseEntity<Object> deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return client.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping(ENDPOINT_ADD_EVENT_TO_COMPILATION)
    public ResponseEntity<Object> addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return client.addEventToCompilation(compId, eventId);
    }

    @PatchMapping(ENDPOINT_PIN_COMPILATION)
    public ResponseEntity<Object> pinCompilation(@PathVariable Long compId) {
        return client.pinCompilation(compId);
    }

    @DeleteMapping(ENDPOINT_UNPIN_COMPILATION)
    public ResponseEntity<Object> unpinCompilation(@PathVariable Long compId) {
        return client.unpinCompilation(compId);
    }
}
