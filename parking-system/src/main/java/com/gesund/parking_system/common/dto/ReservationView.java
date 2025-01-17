package com.gesund.parking_system.common.dto;

import java.time.LocalDateTime;
import com.gesund.parking_system.common.ReservationStatus;

public record ReservationView (String carId, LocalDateTime reservedAt, LocalDateTime expiresAt, ReservationStatus status) {
}

