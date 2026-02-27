package com.railway.controller;

import com.railway.dto.BookingRequest;
import com.railway.dto.BookingResponse;
import com.railway.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request,
            Principal principal) {
        String username = resolveUsername(principal);
        return ResponseEntity.ok(bookingService.createBooking(username, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Principal principal) {
        String username = resolveUsername(principal);
        return ResponseEntity.ok(bookingService.getBookingsForUser(username));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable("id") Long id, Principal principal) {
        String username = resolveUsername(principal);
        return ResponseEntity.ok(bookingService.cancelBooking(username, id));
    }

    private String resolveUsername(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new IllegalArgumentException("Authenticated username required");
        }
        return principal.getName();
    }
}
