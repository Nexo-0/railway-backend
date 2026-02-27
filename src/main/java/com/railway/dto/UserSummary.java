package com.railway.dto;

import java.time.LocalDateTime;

public record UserSummary(Long userId, String username, String role, LocalDateTime createdAt) {}
//admin USER NAME = kabir password = kabir123;
//user guest USER NAME = guest password = guest123;
