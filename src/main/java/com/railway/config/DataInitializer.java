package com.railway.config;

import com.railway.model.Train;
import com.railway.model.User;
import com.railway.repository.TrainRepository;
import com.railway.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Runs on every startup to ensure seed data exists.
 * Safe to run multiple times — uses findByUsername checks to avoid duplicates.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final TrainRepository trainRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
            TrainRepository trainRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedUsers();
        seedTrains();
    }

    private void seedUsers() {
        if (userRepository.findByUsername("kabir").isEmpty()) {
            User admin = new User("kabir", passwordEncoder.encode("kabir123"), "ADMIN");
            userRepository.save(admin);
            log.info("Seeded admin user: kabir / kabir123");
        }
        if (userRepository.findByUsername("guest").isEmpty()) {
            User guest = new User("guest", passwordEncoder.encode("guest123"), "USER");
            userRepository.save(guest);
            log.info("Seeded guest user: guest / guest123");
        }
    }

    private void seedTrains() {
        if (trainRepository.count() == 0) {
            trainRepository.save(buildTrain("Shatabdi Express", "Delhi", "Mumbai",
                    LocalDateTime.of(2026, 3, 10, 6, 0),
                    LocalDateTime.of(2026, 3, 10, 14, 0),
                    new BigDecimal("2500.00"), 240));

            trainRepository.save(buildTrain("Coastal Liner", "Chennai", "Bangalore",
                    LocalDateTime.of(2026, 3, 11, 9, 30),
                    LocalDateTime.of(2026, 3, 11, 13, 30),
                    new BigDecimal("800.00"), 180));

            trainRepository.save(buildTrain("Rajdhani Express", "Mumbai", "Delhi",
                    LocalDateTime.of(2026, 3, 12, 8, 0),
                    LocalDateTime.of(2026, 3, 12, 22, 0),
                    new BigDecimal("3200.00"), 300));

            log.info("Seeded 3 sample trains.");
        }
    }

    private Train buildTrain(String name, String source, String destination,
            LocalDateTime departure, LocalDateTime arrival,
            BigDecimal price, int seats) {
        Train t = new Train();
        t.setTrainName(name);
        t.setSource(source);
        t.setDestination(destination);
        t.setDepartureTime(departure);
        t.setArrivalTime(arrival);
        t.setPrice(price);
        t.setTotalSeats(seats);
        t.setSeatsAvailable(seats);
        return t;
    }
}
