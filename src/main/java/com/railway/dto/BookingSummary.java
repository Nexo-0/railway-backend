package com.railway.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingSummary(
        Long bookingId,
        String username,
        String trainName,
        Integer seatsBooked,
        BigDecimal totalAmount,
        String bookingStatus,
        LocalDateTime bookingDate
) {}
