package com.gesund.parking_system.parking.controller;

import java.time.LocalDateTime;

import com.gesund.parking_system.common.dto.ParkingSlotView;
import com.gesund.parking_system.common.dto.ReservationView;
import com.gesund.parking_system.parking.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingControllerTest {

    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private ParkingController parkingController;

    @Test
    void testGetAvailableSpaces() {
        when(parkingService.getAvailableSpaces()).thenReturn(10);
        final ResponseEntity<String> response = parkingController.getAvailableSpaces();
        assertEquals("Available Parking Spaces - 10", response.getBody());
    }

    @Test
    void testCheckinCar() {
        final String carNumber = "AB1234CD";
        final ParkingSlotView parkingSlotView = new ParkingSlotView(carNumber, LocalDateTime.now(), null);
        when(parkingService.checkinCar(carNumber)).thenReturn(parkingSlotView);
        final ResponseEntity<ParkingSlotView> response = parkingController.checkinCar(carNumber);
        assertEquals(parkingSlotView, response.getBody());
    }

    @Test
    void testCheckoutCar() {
        final String carNumber = "AB1234CD";
        final ParkingSlotView parkingSlotView = new ParkingSlotView(carNumber, null, null);
        when(parkingService.checkoutCar(carNumber)).thenReturn(parkingSlotView);
        final ResponseEntity<ParkingSlotView> response = parkingController.checkoutCar(carNumber);
        assertEquals(parkingSlotView, response.getBody());
    }

    @Test
    void testReserveSpace() {
        final String carNumber = "AB1234CD";
        final ReservationView reservationView = new ReservationView(carNumber, null, null, null);
        when(parkingService.createReservation(carNumber)).thenReturn(reservationView);
        final ResponseEntity<ReservationView> response = parkingController.reserveSpace(carNumber);
        assertEquals(reservationView, response.getBody());
    }

    @Test
    void testCancelReservation() {
        final String carNumber = "AB1234CD";
        final ReservationView reservationView = new ReservationView(carNumber, null, null, null);
        when(parkingService.cancelReservation(carNumber)).thenReturn(reservationView);
        final ResponseEntity<ReservationView> response = parkingController.cancelReservation(carNumber);
        assertEquals(reservationView, response.getBody());
    }
}