package com.example.statistics.controller;

import com.example.statistics.EndpointHit;
import com.example.statistics.ViewStats;
import com.example.statistics.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StatsController {
    private static final String ENDPOINT_SAVE_STATS = "/hit";
    private static final String ENDPOINT_GET_STATS = "/stats";
    private static final String ENDPOINT_GET_VIEW = "/views/{eventId}";
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping(ENDPOINT_SAVE_STATS)
    public EndpointHit saveStats(@RequestBody EndpointHit hit) {
        return statsService.saveStats(hit);
    }

    @GetMapping(ENDPOINT_GET_STATS)
    public List<ViewStats> getStats(@RequestParam(value = "start") String start,
                                    @RequestParam(value = "end") String end,
                                    @RequestParam(value = "uris") List<String> uris,
                                    @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }

    @GetMapping(ENDPOINT_GET_VIEW)
    public Integer getViews(@PathVariable Long eventId) {
        return statsService.getViewsOfEvent(eventId);
    }
}
