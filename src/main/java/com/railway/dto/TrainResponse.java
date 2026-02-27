package com.railway.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TrainResponse(
        Long trainNo,
        String trainName,
        String source,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        BigDecimal price,
        Integer totalSeats,
        Integer seatsAvailable
) {}
