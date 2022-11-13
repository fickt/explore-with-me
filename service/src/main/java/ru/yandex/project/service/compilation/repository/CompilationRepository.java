package ru.yandex.project.service.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.project.service.compilation.model.Compilation;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CompilationRepository extends PagingAndSortingRepository<Compilation, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO EVENT_COMPILATION_TABLE VALUES(:eventId, :compId)", nativeQuery = true)
    void saveEventIdAndCompilationId(@Param("eventId") Long eventId, @Param("compId") Long compId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM EVENT_COMPILATION_TABLE WHERE COMPILATION_ID=:compId", nativeQuery = true)
    void deleteAllById(@Param("compId") Long compId);
    @Query(value = "SELECT COUNT(EVENT_ID) FROM EVENT_COMPILATION_TABLE " +
            "WHERE COMPILATION_ID = :compId AND EVENT_ID = :eventId", nativeQuery = true)
    Long compilationContainsEvent(@Param("compId") Long compId, @Param("eventId")Long eventId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM EVENT_COMPILATION_TABLE WHERE COMPILATION_ID = :compId and EVENT_ID = :eventId", nativeQuery = true)
    void deleteEventFromCompilation(@Param("compId") Long compId, @Param("eventId") Long eventId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO EVENT_COMPILATION_TABLE VALUES(:eventId, :compId)", nativeQuery = true)
    void addEventToCompilation(@Param("compId") Long compId, @Param("eventId") Long eventId);

    List<Compilation> findAllByPinnedIs(Boolean pinned, Pageable pageable);
}
