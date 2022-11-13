package com.example.apigateway.endpointhit;

import lombok.Data;

@Data
public class EndpointHit {
    private Long id;
    private Long eventId;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
