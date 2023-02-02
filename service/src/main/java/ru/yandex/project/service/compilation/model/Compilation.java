package ru.yandex.project.service.compilation.model;

import lombok.*;
import ru.yandex.project.service.compilation.dto.CompilationDto;
import ru.yandex.project.service.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "COMPILATION_TABLE")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "PINNED")
    private Boolean pinned;
    @Column(name = "TITLE")
    private String title;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "EVENT_COMPILATION_TABLE",
            joinColumns = @JoinColumn(name = "COMPILATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID")
    )
    private List<Event> events = new ArrayList<>();

    public CompilationDto toDto() {
        var dto = new CompilationDto();
        dto.setId(id);
        dto.setEvents(events.stream()
                .map(Event::toShortDto)
                .collect(Collectors.toUnmodifiableList()));
        dto.setPinned(pinned);
        dto.setTitle(title);
        return dto;
    }
}
