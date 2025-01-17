package com.gesund.parking_system.parking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.gesund.parking_system.common.domain.ParkingSlot;
import com.gesund.parking_system.common.domain.Reservation;
import com.gesund.parking_system.common.dto.ParkingSlotView;
import com.gesund.parking_system.common.dto.ReservationView;
import com.gesund.parking_system.common.mappers.ParkingSlotMapper;
import com.gesund.parking_system.common.mappers.ReservationMapper;
import com.gesund.parking_system.parking.repository.ParkingRepository;
import com.gesund.parking_system.parking.repository.ReservationRepository;

import org.springframework.stereotype.Service;

import static com.gesund.parking_system.common.ReservationStatus.CANCELLED;
import static com.gesund.parking_system.common.ReservationStatus.CONFIRMED;
import static com.gesund.parking_system.common.ReservationStatus.PENDING;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParkingService {

    private static final String BULGARIAN_LICENSE_PLATE_REGEX = "^[A-Z]{1,2}\\d{4}[A-Z]{2}$";
    private static final Pattern PATTERN = Pattern.compile(BULGARIAN_LICENSE_PLATE_REGEX);
    private static final int PARKING_CAPACITY = 100;
    private static final double UTILIZATION_RATE = 0.8;
    private static final int PARKING_CAPACITY_WITH_UTILIZATION = (int) (PARKING_CAPACITY * UTILIZATION_RATE);

    private final ParkingRepository parkingRepository;
    private final ReservationRepository reservationRepository;
    private final ParkingSlotMapper parkingSlotMapper;
    private final ReservationMapper reservationMapper;

    public int getAvailableSpaces() {
        final int parkedCars = (int) parkingRepository.count();
        final int reservedSpots = reservationRepository.countAllByStatus(PENDING);
        log.info("Getting available parking spaces");

        return PARKING_CAPACITY_WITH_UTILIZATION - parkedCars - reservedSpots;
    }

    @Transactional
    public ParkingSlotView checkinCar(final String carNumber) {
        if(!licenseNumberValidation(carNumber)){
            throw new IllegalArgumentException("Invalid license number");
        }
        reservationCheck(carNumber);

        final ParkingSlot checkinCar = new ParkingSlot();
        checkinCar.setCarId(carNumber);
        parkingRepository.save(checkinCar);
        log.info("Car parked with number: {}", carNumber);

        return parkingSlotMapper.toParkingSlotView(checkinCar);
    }

    @Transactional
    public ParkingSlotView checkoutCar(final String carNumber) {
        final ParkingSlot checkoutCar = getParkingSlot(carNumber);
        checkoutCar.setCheckOut(LocalDateTime.now());
        parkingRepository.delete(checkoutCar);
        log.info("Car checked out with number: {}", carNumber);

        return parkingSlotMapper.toParkingSlotView(checkoutCar);
    }

    @Transactional
    public ReservationView createReservation(final String carId) {
        if(!licenseNumberValidation(carId)){
            throw new IllegalArgumentException("Invalid license number");
        }

        final Optional<Reservation> existingReservation = reservationRepository.findByCarId(carId);

        if (existingReservation.isPresent() && existingReservation.get().getStatus() == PENDING) {
            return reservationMapper.toReservationView(existingReservation.get());
        }

        if (existingReservation.isPresent() && existingReservation.get().getStatus() == CANCELLED) {
            final Reservation reservation = existingReservation.get();
            reservation.setStatus(PENDING);
            return reservationMapper.toReservationView(reservationRepository.save(reservation));
        }

        final Reservation reservation = new Reservation();
        reservation.setCarId(carId);
        reservation.setExpirationTime();

        return reservationMapper.toReservationView(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationView cancelReservation(final String carId) {
        final Reservation reservation = getReservation(carId);

        if(reservation.getStatus() == PENDING){
            reservation.setStatus(CANCELLED);
            return reservationMapper.toReservationView(reservationRepository.save(reservation));
        }

       throw new IllegalArgumentException("Reservation with status %s cannot be cancelled".formatted(reservation.getStatus()));
    }

    private Reservation getReservation(final String carId){
        return reservationRepository.findByCarId(carId).orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }

    private ParkingSlot getParkingSlot(final String carId){
        return parkingRepository.findByCarId(carId).orElseThrow(() -> new EntityNotFoundException("Parking slot not found"));
    }

    private void reservationCheck(final String carNumber) {
        reservationRepository.findByCarId(carNumber)
                .ifPresent(reservation ->{
                    reservation.setStatus(CONFIRMED);
                    reservationRepository.save(reservation);
                });
    }

    private static boolean licenseNumberValidation(final String number) {
        if (number == null) {
            return false;
        }
        final Matcher matcher = PATTERN.matcher(number);
        return matcher.matches();
    }
}
