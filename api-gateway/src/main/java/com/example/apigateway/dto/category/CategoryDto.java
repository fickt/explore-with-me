package com.example.apigateway.dto.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A class used to edit already existing categories
 */
@Data
public class CategoryDto {
    @NotNull(message = "id should not be empty")
    private Long id;
    @NotBlank(message = "name should not be empty")
    private String name;
}
