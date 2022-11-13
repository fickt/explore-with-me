package com.example.apigateway.dto.eventdto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Location {
    @NotNull(message = "Latitude should not be empty")
    private Float lat;
    @NotNull(message = "Longitude should not be empty")
    private Float lon;
}
