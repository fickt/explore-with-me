package ru.yandex.project.service.statistics;

import lombok.Data;

@Data
public class EndpointHit {
    private Long id;
    private String app;
    private Long eventId;
    private String uri;
    private String ip;
    private String timestamp;
}
