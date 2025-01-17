package com.gesund.parking_system.parking.service;

import com.gesund.parking_system.common.domain.ParkingSlot;
import com.gesund.parking_system.common.domain.Reservation;
import com.gesund.parking_system.common.dto.ParkingSlotView;
import com.gesund.parking_system.common.dto.ReservationView;
import com.gesund.parking_system.common.mappers.ParkingSlotMapper;
import com.gesund.parking_system.common.mappers.ReservationMapper;
import com.gesund.parking_system.parking.repository.ParkingRepository;
import com.gesund.parking_system.parking.repository.ReservationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.gesund.parking_system.common.ReservationStatus.CANCELLED;
import static com.gesund.parking_system.common.ReservationStatus.PENDING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private ParkingRepository parkingRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ParkingSlotMapper parkingSlotMapper;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ParkingService parkingService;

    @Test
    void getAvailableSpaces_shouldReturnAvailableSpaces() {
        when(parkingRepository.count()).thenReturn(50L);
        when(reservationRepository.countAllByStatus(PENDING)).thenReturn(10);

        final int availableSpaces = parkingService.getAvailableSpaces();

        assertEquals(20, availableSpaces);
    }

    @Test
    void checkinCar_whenCarNumberIsCorrect_shouldCheckinCar() {
        final String carNumber = "AB1234CD";
        final ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setCarId(carNumber);
        final ParkingSlotView parkingSlotView = new ParkingSlotView(carNumber, LocalDateTime.now(), null);

        when(parkingRepository.save(any(ParkingSlot.class))).thenReturn(parkingSlot);
        when(parkingSlotMapper.toParkingSlotView(parkingSlot)).thenReturn(parkingSlotView);

        final ParkingSlotView result = parkingService.checkinCar(carNumber);

        assertEquals(parkingSlotView, result);
        verify(parkingRepository, times(1)).save(any(ParkingSlot.class));
    }

    @Test
    void checkoutCar_whenCarIsCheckedIn_shouldCheckOutCar() {
        final String carNumber = "AB1234CD";
        final ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setCarId(carNumber);
        parkingSlot.setCheckOut(LocalDateTime.now());
        final ParkingSlotView parkingSlotView = new ParkingSlotView(carNumber, null, LocalDateTime.now());

        when(parkingRepository.findByCarId(carNumber)).thenReturn(Optional.of(parkingSlot));
        when(parkingSlotMapper.toParkingSlotView(parkingSlot)).thenReturn(parkingSlotView);

        final ParkingSlotView result = parkingService.checkoutCar(carNumber);

        assertEquals(parkingSlotView, result);
        verify(parkingRepository, times(1)).delete(parkingSlot);
    }

    @Test
    void createReservation_whenCarNumberIsCorrect_shouldCreateReservation() {
        final String carId = "AB1234CD";
        final Reservation reservation = new Reservation();
        reservation.setCarId(carId);
        reservation.setStatus(PENDING);
        final ReservationView reservationView = new ReservationView(carId, null, null, null);

        when(reservationRepository.findByCarId(carId)).thenReturn(Optional.empty());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toReservationView(reservation)).thenReturn(reservationView);

        final ReservationView result = parkingService.createReservation(carId);

        assertEquals(reservationView, result);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void cancelReservation_whenReservationExist_shouldCancelReservation() {
        final String carId = "AB1234CD";
        final Reservation reservation = new Reservation();
        reservation.setCarId(carId);
        reservation.setStatus(PENDING);
        final ReservationView reservationView = new ReservationView(carId, null, null, null);

        when(reservationRepository.findByCarId(carId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(reservationMapper.toReservationView(reservation)).thenReturn(reservationView);

        final ReservationView result = parkingService.cancelReservation(carId);

        assertEquals(reservationView, result);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void cancelReservation_whenReservationDoesNotExist_shouldThrowsException() {
        final String carId = "AB1234CD";
        final Reservation reservation = new Reservation();
        reservation.setCarId(carId);
        reservation.setStatus(CANCELLED);

        when(reservationRepository.findByCarId(carId)).thenReturn(Optional.of(reservation));

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parkingService.cancelReservation(carId);
        });

        assertEquals("Reservation with status CANCELLED cannot be cancelled", exception.getMessage());
    }
}