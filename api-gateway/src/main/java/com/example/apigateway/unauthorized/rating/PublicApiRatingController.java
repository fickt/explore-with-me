package com.example.apigateway.unauthorized.rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
@Slf4j
public class PublicApiRatingController {

    private final PublicApiRatingClient client;

    public PublicApiRatingController(PublicApiRatingClient client) {
        this.client = client;
    }

    @GetMapping
    public ResponseEntity<Object> getSortedEventsWithRating(@RequestParam(value = "sort") String sort,
                                                            @RequestParam(value = "size") Long size) {
        log.info("GET /ratings?sort={}&size={}", sort.isBlank() ? "null" : sort, size);
        return client.getSortedEventsWithRating(sort, size);
    }
}
