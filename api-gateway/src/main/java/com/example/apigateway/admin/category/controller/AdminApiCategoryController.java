package com.example.apigateway.admin.category.controller;

import com.example.apigateway.admin.category.client.AdminApiCategoryClient;
import com.example.apigateway.dto.category.CategoryDto;
import com.example.apigateway.dto.category.NewCategoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/categories")
public class AdminApiCategoryController {
    private final AdminApiCategoryClient client;

    @Autowired
    public AdminApiCategoryController(AdminApiCategoryClient client) {
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<Object> addCategory(@RequestBody @Valid NewCategoryDto categoryDto) {
        log.info(String.format("POST /admin/categories sent data: %s", categoryDto));
        return client.addCategory(categoryDto);
    }

    @PatchMapping
    public ResponseEntity<Object> editCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info(String.format("PATCH /admin/categories sent data: %s", categoryDto));
        return client.editCategory(categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PositiveOrZero @PathVariable Long categoryId) {
        log.info(String.format("DELETE /admin/categories categoryId: %s", categoryId));
        client.deleteCategory(categoryId);
    }

}
