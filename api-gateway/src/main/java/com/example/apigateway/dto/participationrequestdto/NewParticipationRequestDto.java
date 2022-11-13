package com.example.apigateway.dto.participationrequestdto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NewParticipationRequestDto {
    @DateTimeFormat(pattern = "uuuu-MM-dd hh:mm:ss")
    private LocalDateTime created;
    @NotNull(message = "event id should not be empty")
    private Long event;
    private Long requester;
}
