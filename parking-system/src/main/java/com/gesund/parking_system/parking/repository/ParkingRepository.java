package com.gesund.parking_system.parking.repository;

import java.util.Optional;

import com.gesund.parking_system.common.domain.ParkingSlot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingSlot, Long> {
    Optional<ParkingSlot> findByCarId(String carId);
}
