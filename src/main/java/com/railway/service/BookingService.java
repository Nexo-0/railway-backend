package com.railway.service;

import com.railway.dto.BookingRequest;
import com.railway.dto.BookingResponse;
import com.railway.dto.BookingSummary;
import com.railway.model.Booking;
import com.railway.model.Payment;
import com.railway.model.Train;
import com.railway.model.User;
import com.railway.repository.BookingRepository;
import com.railway.repository.PaymentRepository;
import com.railway.repository.TrainRepository;
import com.railway.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final TrainRepository trainRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          PaymentRepository paymentRepository,
                          TrainRepository trainRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.trainRepository = trainRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BookingResponse createBooking(String username, BookingRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Train train = trainRepository.findById(request.trainNo())
                .orElseThrow(() -> new IllegalArgumentException("Train not found"));

        int seatsRequested = request.seatsBooked();
        if (seatsRequested <= 0) {
            throw new IllegalArgumentException("Seats must be at least 1");
        }
        if (train.getSeatsAvailable() < seatsRequested) {
            throw new IllegalStateException("Not enough seats available");
        }

        train.setSeatsAvailable(train.getSeatsAvailable() - seatsRequested);
        trainRepository.save(train);

        BigDecimal totalAmount = train.getPrice().multiply(BigDecimal.valueOf(seatsRequested));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrain(train);
        booking.setSeatsBooked(seatsRequested);
        booking.setTotalAmount(totalAmount);
        booking.setBookingStatus("CONFIRMED");

        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentStatus("PENDING");
        payment.setBooking(booking);
        booking.setPayment(payment);

        Booking saved = bookingRepository.save(booking);

        return toResponse(saved);
    }

    public List<BookingResponse> getBookingsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return bookingRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public BookingResponse cancelBooking(String username, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        if (!booking.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("Not authorized to cancel this booking");
        }
        if ("CANCELLED".equalsIgnoreCase(booking.getBookingStatus())) {
            return toResponse(booking);
        }

        Train train = booking.getTrain();
        train.setSeatsAvailable(train.getSeatsAvailable() + booking.getSeatsBooked());
        trainRepository.save(train);

        booking.setBookingStatus("CANCELLED");
        if (booking.getPayment() != null) {
            booking.getPayment().setPaymentStatus("REFUNDED");
            paymentRepository.save(booking.getPayment());
        }

        return toResponse(bookingRepository.save(booking));
    }

    public List<BookingSummary> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toSummary)
                .toList();
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getTrain().getTrainNo(),
                booking.getTrain().getTrainName(),
                booking.getSeatsBooked(),
                booking.getTotalAmount(),
                booking.getBookingStatus(),
                booking.getBookingDate()
        );
    }

    private BookingSummary toSummary(Booking booking) {
        return new BookingSummary(
                booking.getBookingId(),
                booking.getUser().getUsername(),
                booking.getTrain().getTrainName(),
                booking.getSeatsBooked(),
                booking.getTotalAmount(),
                booking.getBookingStatus(),
                booking.getBookingDate()
        );
    }
}
