package com.gesund.parking_system.common.dto;

import java.time.LocalDateTime;

public record ParkingSlotView(String carId, LocalDateTime checkIn, LocalDateTime checkOut) {
}
