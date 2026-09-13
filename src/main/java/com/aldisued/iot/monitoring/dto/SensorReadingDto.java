package com.aldisued.iot.monitoring.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record SensorReadingDto(
    @NotNull(message = "Sensor ID must not be null") UUID sensorId,
    Double value,
    LocalDateTime timestamp
) {}
