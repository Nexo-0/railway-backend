package com.railway.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "Train number is required")
        Long trainNo,

        @NotNull(message = "Number of seats is required")
        @Min(value = 1, message = "At least 1 seat must be booked")
        Integer seatsBooked
) {}
