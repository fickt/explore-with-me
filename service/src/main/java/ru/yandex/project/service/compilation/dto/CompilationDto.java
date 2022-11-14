package ru.yandex.project.service.compilation.dto;

import lombok.Data;
import ru.yandex.project.service.event.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private List<EventShortDto> events = new ArrayList<>();
}
