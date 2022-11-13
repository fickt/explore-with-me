package com.example.statistics.service;

import com.example.statistics.EndpointHit;
import com.example.statistics.ViewStats;

import java.util.List;

public interface StatsService {
    EndpointHit saveStats(EndpointHit hit);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);

    Integer getViewsOfEvent(Long eventId);
}
