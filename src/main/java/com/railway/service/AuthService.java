package com.railway.service;

import com.railway.dto.AuthRequest;
import com.railway.dto.AuthResponse;
import com.railway.dto.RegisterRequest;
import com.railway.model.User;
import com.railway.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User(request.username(),
                passwordEncoder.encode(request.password()),
                "USER");
        return userRepository.save(user);
    }
}
