package com.railway.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TrainRequest(
        @NotBlank(message = "Train name is required")
        String trainName,

        @NotBlank(message = "Source station is required")
        String source,

        @NotBlank(message = "Destination station is required")
        String destination,

        @NotNull(message = "Departure time is required")
        LocalDateTime departureTime,

        @NotNull(message = "Arrival time is required")
        LocalDateTime arrivalTime,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Total seats is required")
        @Min(value = 1, message = "Must have at least 1 seat")
        Integer totalSeats
) {}
