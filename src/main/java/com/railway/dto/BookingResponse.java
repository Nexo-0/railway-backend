package com.railway.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponse(
        Long bookingId,
        Long trainNo,
        String trainName,
        Integer seatsBooked,
        BigDecimal totalAmount,
        String bookingStatus,
        LocalDateTime bookingDate
) {}
