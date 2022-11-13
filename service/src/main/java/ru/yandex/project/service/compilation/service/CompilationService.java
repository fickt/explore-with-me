package ru.yandex.project.service.compilation.service;

import ru.yandex.project.service.compilation.dto.CompilationDto;
import ru.yandex.project.service.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Long from, Long size);

    CompilationDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto deleteEventFromCompilation(Long compId, Long eventId);

    CompilationDto addEventToCompilation(Long compId, Long eventId);

    CompilationDto pinCompilation(Long compId);

    CompilationDto unpinCompilation(Long compId);

    CompilationDto getCompilationById(Long compId);
}
