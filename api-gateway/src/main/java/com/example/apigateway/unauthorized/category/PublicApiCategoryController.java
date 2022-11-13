package com.example.apigateway.unauthorized.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/categories")
public class PublicApiCategoryController {
    private static final String ENDPOINT_GET_CATEGORY_BY_ID = "/{categoryId}";
    private final PublicApiCategoryClient client;

    @Autowired
    public PublicApiCategoryController(PublicApiCategoryClient client) {
        this.client = client;
    }


    @GetMapping
    public ResponseEntity<Object> getCategories(@PositiveOrZero @RequestParam(value = "from", defaultValue = "1") Long from,
                                                @Positive @RequestParam(value = "size", defaultValue = "10") Long size) {
        return client.getCategories(from, size);
    }

    @GetMapping(ENDPOINT_GET_CATEGORY_BY_ID)
    public ResponseEntity<Object> getCategoryById(@PathVariable Long categoryId) {
        return client.getCategoryById(categoryId);
    }
}
