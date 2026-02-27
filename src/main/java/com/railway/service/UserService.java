package com.railway.service;

import com.railway.dto.UserSummary;
import com.railway.model.User;
import com.railway.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserSummary> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toSummary)
                .toList();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private UserSummary toSummary(User user) {
        return new UserSummary(user.getUserId(), user.getUsername(), user.getRole(), user.getCreatedAt());
    }
}
