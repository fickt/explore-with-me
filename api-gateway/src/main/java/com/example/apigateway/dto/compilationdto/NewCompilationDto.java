package com.example.apigateway.dto.compilationdto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Data
public class NewCompilationDto {
    @NotBlank(message = "title should not be empty")
    private String title;
    private boolean pinned;
    private Set<Integer> events = new HashSet<>();
}
