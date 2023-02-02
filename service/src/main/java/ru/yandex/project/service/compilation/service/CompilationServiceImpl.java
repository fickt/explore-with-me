package ru.yandex.project.service.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.project.service.compilation.dto.CompilationDto;
import ru.yandex.project.service.compilation.dto.NewCompilationDto;
import ru.yandex.project.service.compilation.model.Compilation;
import ru.yandex.project.service.compilation.repository.CompilationRepository;
import ru.yandex.project.service.event.model.Event;
import ru.yandex.project.service.event.repository.EventRepository;
import ru.yandex.project.service.exception.NotFoundException;


import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.project.service.exception.Message.COMPILATION_NOT_FOUND;
import static ru.yandex.project.service.exception.Message.EVENT_NOT_FOUND;


@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return compilationRepository.findById(compId)
                .map(Compilation::toDto)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_FOUND.get(), compId)));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Long from, Long size) {
        return compilationRepository.findAllByPinnedIs(pinned, PageRequest.of(from.intValue(), size.intValue())).stream()
                .map(Compilation::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        Compilation compilationEntity = compilationDto.toEntity();
        compilationDto.getEvents()                               //converting eventId to List<Event> and attaching it to Compilation
                .forEach(eventId -> compilationEntity.getEvents().add(getEventById(eventId)));

        Compilation compilation = compilationRepository.save(compilationEntity);
        log.info("compilation after saving in DB: {}", compilation);
        return compilation.toDto();
    }

    @Override
    public CompilationDto deleteEventFromCompilation(Long compId, Long eventId) {
        var compilation = getCompilation(compId);
        var compilationDto = compilation.toDto();
        var event = getEventById(eventId.intValue());

        if (compilation.getEvents().contains(event)) {
            compilation.getEvents().remove(event);
            compilationRepository.save(compilation);
        }  else {
            throw new NotFoundException(String.format(COMPILATION_NOT_FOUND.get(), compId, eventId));
        }
        return compilationDto;
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto addEventToCompilation(Long compId, Long eventId) {
        var compilation = getCompilation(compId);
        var compilationDto = compilation.toDto();
        compilation.getEvents().add(getEventById(eventId.intValue()));
        compilationRepository.save(compilation);
        return compilationDto;
    }

    @Override
    public CompilationDto pinCompilation(Long compId) {
        var compilation = getCompilation(compId);
        log.info("pinCompilation {}", compilation);
        var compilationDto = compilation.toDto();
        compilation.setPinned(Boolean.TRUE);
        compilationRepository.save(compilation);
        return compilationDto;
    }


    @Override
    public CompilationDto unpinCompilation(Long compId) {
        var compilation = getCompilation(compId);
        var compilationDto = compilation.toDto();
        compilation.setPinned(Boolean.FALSE);
        compilationRepository.save(compilation);
        return compilationDto;
    }

    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_FOUND.get(), compId)));
    }

    private Event getEventById(Integer eventId) {
        return eventRepository.findById(eventId.longValue())
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND.get(), eventId)));
    }
}
