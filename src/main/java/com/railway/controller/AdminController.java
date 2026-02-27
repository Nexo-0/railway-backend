package com.railway.controller;

import com.railway.dto.BookingSummary;
import com.railway.dto.RevenueResponse;
import com.railway.dto.TrainRequest;
import com.railway.dto.TrainResponse;
import com.railway.dto.UserSummary;
import com.railway.service.BookingService;
import com.railway.service.ReportingService;
import com.railway.service.TrainService;
import com.railway.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {
    private final TrainService trainService;
    private final BookingService bookingService;
    private final UserService userService;
    private final ReportingService reportingService;

    public AdminController(TrainService trainService,
            BookingService bookingService,
            UserService userService,
            ReportingService reportingService) {
        this.trainService = trainService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.reportingService = reportingService;
    }

    @PostMapping("/trains")
    public ResponseEntity<TrainResponse> createTrain(@Valid @RequestBody TrainRequest request) {
        return ResponseEntity.status(201).body(trainService.createTrain(request));
    }

    @PutMapping("/trains/{id}")
    public ResponseEntity<TrainResponse> updateTrain(@PathVariable("id") Long id,
            @Valid @RequestBody TrainRequest request) {
        return ResponseEntity.ok(trainService.updateTrain(id, request));
    }

    @DeleteMapping("/trains/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable("id") Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingSummary>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserSummary>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueResponse> getRevenue() {
        return ResponseEntity.ok(reportingService.getRevenue());
    }
}
