package com.example.statistics.repository;

import com.example.statistics.EndpointHit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface StatsRepository extends PagingAndSortingRepository<EndpointHit, Long> {

    @Query(value = "SELECT DISTINCT COUNT(IP) FROM ENDPOINT_STATS_TABLE " +
            "WHERE (URI = :URI) AND" +
            "(TIME_STAMP >= :start AND TIME_STAMP <= :end)", nativeQuery = true)
    int getStatsUniqueIp(@Param("start") LocalDateTime start,
                         @Param("end") LocalDateTime end,
                         @Param("URI") String URI);

    @Query(value = "SELECT COUNT(IP) FROM ENDPOINT_STATS_TABLE " +
            "WHERE (URI = :URI) AND" +
            "(TIME_STAMP >= :start AND TIME_STAMP <= :end)", nativeQuery = true)
    int getStats(@Param("start") LocalDateTime start,
                 @Param("end") LocalDateTime end,
                 @Param("URI") String URI);

    @Query(value = "SELECT APP FROM ENDPOINT_STATS_TABLE WHERE URI = :URI", nativeQuery = true)
    String getApp(@Param("URI") String URI);

    @Query(value = "SELECT COUNT(IP) FROM ENDPOINT_STATS_TABLE WHERE EVENT_ID = :eventId", nativeQuery = true)
    int getViewsOfEvent(@Param("eventId") Long eventId);
}
