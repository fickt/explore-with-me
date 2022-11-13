package com.example.statistics.service;

import com.example.statistics.EndpointHit;
import com.example.statistics.repository.StatsRepository;
import com.example.statistics.ViewStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    private static final  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    @Autowired
    public StatsServiceImpl(StatsRepository repository) {
        this.repository = repository;
    }

    @Override
    public EndpointHit saveStats(EndpointHit hit) {
        return repository.save(hit);
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startAsLocalDateTime = null;
        LocalDateTime endAsLocalDateTime = null;

        if (start != null) {
             startAsLocalDateTime = LocalDateTime.parse(start, DATE_TIME_FORMATTER);
        }

        if (end != null) {
            endAsLocalDateTime = LocalDateTime.parse(end, DATE_TIME_FORMATTER);
        }

        var result = new ArrayList<ViewStats>();
        if (unique.equals(Boolean.TRUE)) {

            for (String URI : uris) {
                int hits = repository.getStatsUniqueIp(startAsLocalDateTime, endAsLocalDateTime, URI);
                String app = repository.getApp(URI);
                var stats = new ViewStats();
                stats.setApp(app);
                stats.setUri(URI);
                stats.setHits(hits);
                result.add(stats);
            }
        } else {
            for (String URI : uris) {
                int hits = repository.getStats(startAsLocalDateTime, endAsLocalDateTime, URI);
                String app = repository.getApp(URI);
                var stats = new ViewStats();
                stats.setApp(app);
                stats.setUri(URI);
                stats.setHits(hits);
                result.add(stats);
            }
        }
        return result;
    }

    @Override
    public Integer getViewsOfEvent(Long eventId) {
        return repository.getViewsOfEvent(eventId);
    }


}
