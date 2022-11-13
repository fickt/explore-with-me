package ru.yandex.project.service.category.dto;

import lombok.Data;
import ru.yandex.project.service.category.model.Category;

@Data
public class CategoryDto {
    private Long id;
    private String name;

    public Category toCategoryEntity() {
        var category = new Category();
        category.setName(name);
        return category;
    }
}
