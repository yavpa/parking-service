package com.gesund.parking_system.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.gesund.parking_system.common.ReservationStatus;

import org.hibernate.annotations.CreationTimestamp;

import static com.gesund.parking_system.common.ReservationStatus.PENDING;


@Entity
@Table(name = "reservations")
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String carId;

    private LocalDateTime reservedAt = LocalDateTime.now();
    private LocalDateTime expiresAt;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = PENDING;

    public void setExpirationTime() {
        expiresAt = reservedAt.plusHours(1L);
    }
}
