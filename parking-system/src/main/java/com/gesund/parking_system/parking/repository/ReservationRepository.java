package com.gesund.parking_system.parking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.gesund.parking_system.common.domain.Reservation;
import com.gesund.parking_system.common.ReservationStatus;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByCarId(String carId);

    int countAllByStatus(ReservationStatus status);

    List<Reservation> findByExpiresAtBeforeAndStatus(LocalDateTime now, ReservationStatus status);
}
