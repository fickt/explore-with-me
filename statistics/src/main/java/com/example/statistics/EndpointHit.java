package com.example.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ENDPOINT_STATS_TABLE")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "APP")
    private String app;
    @Column(name = "EVENT_ID")
    @JsonIgnore
    private transient Long eventId;
    @Column(name = "URI")
    private String uri;
    @Column(name = "IP")
    private String ip;
    @Column(name = "TIME_STAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
