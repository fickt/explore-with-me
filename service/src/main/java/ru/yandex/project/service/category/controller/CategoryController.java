package ru.yandex.project.service.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.project.service.category.dto.CategoryDto;
import ru.yandex.project.service.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private static final String ENDPOINT_GET_CATEGORY = "/{categoryId}";
    private static final String ENDPOINT_DELETE_CATEGORY = "/{categoryId}";
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(ENDPOINT_GET_CATEGORY)
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(value = "from") Long from,
                                           @RequestParam(value = "size") Long size) {
        return categoryService.getCategories(from, size);
    }

    @PostMapping
    public CategoryDto addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping
    public CategoryDto editCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.editCategory(categoryDto);
    }

    @DeleteMapping(ENDPOINT_DELETE_CATEGORY)
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
