package com.example.apigateway.dto.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * A class used to create new categories
 */
@Data
public class NewCategoryDto {
    @NotBlank
    private String name;
}
