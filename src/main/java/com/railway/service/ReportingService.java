package com.railway.service;

import com.railway.dto.RevenueResponse;
import com.railway.model.Booking;
import com.railway.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReportingService {
    private final BookingRepository bookingRepository;

    public ReportingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public RevenueResponse getRevenue() {
        var bookings = bookingRepository.findAll();
        var confirmedBookings = bookings.stream()
                .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getBookingStatus()))
                .toList();
        BigDecimal total = confirmedBookings.stream()
                .map(Booking::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new RevenueResponse(total, confirmedBookings.size());
    }
}
