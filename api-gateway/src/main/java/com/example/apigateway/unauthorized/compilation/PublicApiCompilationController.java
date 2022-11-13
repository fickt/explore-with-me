package com.example.apigateway.unauthorized.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequestMapping("/compilations")
public class PublicApiCompilationController {
    private static final String ENDPOINT_GET_COMPILATION_BY_ID = "/{compId}";

    private final PublicApiCompilationClient client;

    @Autowired
    public PublicApiCompilationController(PublicApiCompilationClient client) {
        this.client = client;
    }

    @GetMapping
    public ResponseEntity<Object> getCompilations(@RequestParam(value = "pinned", defaultValue = "false") Boolean pinned,
                                                  @RequestParam(value = "from", defaultValue = "1") Long from,
                                                  @RequestParam(value = "size", defaultValue = "10") Long size) {
        return client.getCompilations(pinned, from, size);
    }

    @GetMapping(ENDPOINT_GET_COMPILATION_BY_ID)
    public ResponseEntity<Object> getCompilationById(@Positive @PathVariable Long compId) {
        return client.getCompilationById(compId);
    }
}
