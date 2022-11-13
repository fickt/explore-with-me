package ru.yandex.project.service.compilation.dto;

import lombok.Data;
import ru.yandex.project.service.compilation.model.Compilation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
public class NewCompilationDto {
    @NotBlank
    private String title;
    private boolean pinned;
    @NotEmpty
    private Set<Integer> events = new HashSet<>();

    public Compilation toEntity() {
        var compilation = new Compilation();
        compilation.setPinned(pinned);
        compilation.setTitle(title);
        return compilation;
    }
}
