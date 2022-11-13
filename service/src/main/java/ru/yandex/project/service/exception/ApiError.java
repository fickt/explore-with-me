package ru.yandex.project.service.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private String status;
    private String reason;
    private String message;
    @JsonProperty("timestamp")
    @DateTimeFormat(pattern = "uuuu-MM-dd hh:mm:ss")
    private LocalDateTime timeStamp = LocalDateTime.now();
}
