package ru.yandex.project.service.category.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.project.service.category.dto.CategoryDto;
import ru.yandex.project.service.category.model.Category;
import ru.yandex.project.service.category.repository.CategoryRepository;
import ru.yandex.project.service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.project.service.exception.Message.CATEGORY_NOT_FOUND;
import static ru.yandex.project.service.exception.Message.EVENT_NOT_FOUND;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return repository.findById(categoryId)
                .map(Category::toDto)
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND.get(), categoryId)));
    }

    @Override
    public List<CategoryDto> getCategories(Long from, Long size) {
        return repository.findAll(PageRequest.of(from.intValue(), size.intValue())).stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        return repository.save(categoryDto.toCategoryEntity()).toDto();
    }

    @Override
    public CategoryDto editCategory(CategoryDto categoryDto) {
        repository.findById(categoryDto.getId()).ifPresentOrElse(category -> {
                    category.setName(categoryDto.getName());
                    repository.save(category);
                },
                () -> {
                    throw new NotFoundException(String.format(EVENT_NOT_FOUND.get(), categoryDto.getId()));
                });
        return categoryDto;
    }

    @Override
    public void deleteCategory(Long categoryId) {
        repository.deleteById(categoryId);
    }
}
