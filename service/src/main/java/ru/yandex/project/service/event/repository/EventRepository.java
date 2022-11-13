package ru.yandex.project.service.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.project.service.event.model.Event;
import ru.yandex.project.service.event.status.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long initiatorId);

    @Query(value = "FROM Event e " +
            "WHERE LOWER(e.annotation) LIKE LOWER(:text) OR LOWER(e.description) LIKE LOWER(:text) " +
            "AND (:status IS NULL OR e.state = :status) " +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND e.eventDate >= :rangeStart " +
            "AND e.eventDate <= :rangeEnd " +
            "AND e.paid = :paid " +
            "AND e.participantLimit < e.confirmedRequests")
    List<Event> getOnlyAvailableEvents(Pageable pageable,
                                       @Param("text") String text,
                                       @Param("status") EventStatus status,
                                       @Param("categories") List<Long> categories,
                                       @Param("paid") Boolean paid,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd
    );

    @Query(value = "FROM Event e " +
            "WHERE LOWER(e.annotation) LIKE LOWER(:text) OR LOWER(e.description) LIKE LOWER(:text) " +
            "AND (:status IS NULL or e.state = :status) " +
            "AND (:categories IS NULL or e.category.id IN (:categories)) " +
            "AND e.eventDate >= :rangeStart " +
            "AND e.eventDate <= :rangeEnd " +
            "AND e.paid = :paid ")
    List<Event> getAllEvents(Pageable pageable,
                             @Param("text") String text,
                             @Param("status") EventStatus status,
                             @Param("categories") List<Long> categories,
                             @Param("paid") Boolean paid,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             @Param("rangeEnd") LocalDateTime rangeEnd
    );

    @Query(value = "FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator.id IN (:users)) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND (e.eventDate >= :rangeStart AND e.eventDate <= :rangeEnd)")
    List<Event> getAllEventsAdmin(Pageable pageable,
                                  @Param("users") List<Long> users,
                                  @Param("states") List<EventStatus> states,
                                  @Param("categories") List<Long> categories,
                                  @Param("rangeStart") LocalDateTime rangeStartAsLocalDateTime,
                                  @Param("rangeEnd") LocalDateTime rangeEndAsLocalDateTime);
}
