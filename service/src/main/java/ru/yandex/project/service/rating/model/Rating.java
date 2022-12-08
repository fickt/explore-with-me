package ru.yandex.project.service.rating.model;

import lombok.*;
import ru.yandex.project.service.event.model.Event;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "LIKE_DISLIKE_TOTAL_TABLE")
public class Rating {
    @Id
    @Column(name ="EVENT_ID")
    private Long eventId;
    @Column(name = "LIKE_TOTAL")
    private long likes;
    @Column(name = "DISLIKE_TOTAL")
    private long dislikes;
    @JoinColumn(name = "EVENT_ID")
    @OneToOne
    private Event event;
}
