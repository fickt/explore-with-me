package ru.yandex.project.service.category.service;

import ru.yandex.project.service.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto editCategory(CategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    List<CategoryDto> getCategories(Long from, Long size);

    CategoryDto getCategoryById(Long categoryId);
}
