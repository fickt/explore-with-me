package ru.yandex.project.service.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.project.service.compilation.dto.CompilationDto;
import ru.yandex.project.service.compilation.dto.NewCompilationDto;
import ru.yandex.project.service.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
public class CompilationController {
    private static final String ENDPOINT_GET_COMPILATION_BY_ID = "/{compId}";
    private static final String ENDPOINT_DELETE_COMPILATION_BY_ID = "/{compId}";
    private static final String ENDPOINT_DELETE_EVENT_FROM_COMPILATION = "/{compId}/events/{eventId}";
    private static final String ENDPOINT_ADD_EVENT_TO_COMPILATION = "/{compId}/events/{eventId}";
    private static final String ENDPOINT_PIN_COMPILATION = "/{compId}/pin";
    private static final String ENDPOINT_UNPIN_COMPILATION = "/{compId}/pin";
    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                                @RequestParam(value = "from") Long from,
                                                @RequestParam(value = "size") Long size) {

        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping(ENDPOINT_GET_COMPILATION_BY_ID)
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }

    @PostMapping
    public CompilationDto addCompilation(@RequestBody NewCompilationDto compilationDto) {
        log.info("POST /compilations compilationDto: {}", compilationDto);
        return compilationService.addCompilation(compilationDto);
    }

    @DeleteMapping(ENDPOINT_DELETE_COMPILATION_BY_ID)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping(ENDPOINT_DELETE_EVENT_FROM_COMPILATION)
    public CompilationDto deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping(ENDPOINT_ADD_EVENT_TO_COMPILATION)
    public CompilationDto addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return compilationService.addEventToCompilation(compId, eventId);
    }

    @PatchMapping(ENDPOINT_PIN_COMPILATION)
    public CompilationDto pinCompilation(@PathVariable Long compId) {
        return compilationService.pinCompilation(compId);
    }

    @DeleteMapping(ENDPOINT_UNPIN_COMPILATION)
    public CompilationDto unpinCompilation(@PathVariable Long compId) {
        return compilationService.unpinCompilation(compId);
    }
}
