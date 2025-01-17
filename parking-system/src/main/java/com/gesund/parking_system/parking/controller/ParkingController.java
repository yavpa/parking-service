package com.gesund.parking_system.parking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.gesund.parking_system.common.dto.ParkingSlotView;
import com.gesund.parking_system.common.dto.ReservationView;
import com.gesund.parking_system.parking.service.ParkingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @GetMapping
    public ResponseEntity<String> getAvailableSpaces() {
        return ResponseEntity.ok("Available Parking Spaces - %s".formatted(parkingService.getAvailableSpaces()));
    }

    @PostMapping("/checkin/{carNumber}")
    public ResponseEntity<ParkingSlotView> checkinCar(@PathVariable final String carNumber) {
        return ResponseEntity.ok(parkingService.checkinCar(carNumber));
    }

    @PutMapping("/checkout/{carNumber}")
    public ResponseEntity<ParkingSlotView> checkoutCar(@PathVariable final String carNumber) {
        return ResponseEntity.ok(parkingService.checkoutCar(carNumber));
    }

    @PostMapping("/reserve/{carNumber}")
    public ResponseEntity<ReservationView> reserveSpace(@PathVariable final String carNumber) {
        return ResponseEntity.ok(parkingService.createReservation(carNumber));
    }

    @PutMapping("/reserve/cancel/{carNumber}")
    public ResponseEntity<ReservationView> cancelReservation(@PathVariable final String carNumber) {
        return ResponseEntity.ok(parkingService.cancelReservation(carNumber));
    }
}
