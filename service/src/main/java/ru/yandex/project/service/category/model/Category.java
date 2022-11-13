package ru.yandex.project.service.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.project.service.category.dto.CategoryDto;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORY_TABLE")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NAME", unique = true)
    private String name;

    public CategoryDto toDto() {
        var categoryDto = new CategoryDto();
        categoryDto.setId(id);
        categoryDto.setName(name);
        return categoryDto;
    }
}
