package com.railway.dto;

import java.math.BigDecimal;

public record RevenueResponse(BigDecimal totalRevenue, Integer totalBookings) {}
