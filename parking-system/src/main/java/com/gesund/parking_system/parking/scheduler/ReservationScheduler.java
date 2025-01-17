package com.gesund.parking_system.parking.scheduler;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.gesund.parking_system.common.domain.Reservation;
import com.gesund.parking_system.parking.repository.ReservationRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.gesund.parking_system.common.ReservationStatus.EXPIRED;
import static com.gesund.parking_system.common.ReservationStatus.PENDING;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;

    @Scheduled(cron = "0 * * * * *")
    public void expiredReservationsCheck() {
        final LocalDateTime now = LocalDateTime.now();
        final List<Reservation> expiredReservations = reservationRepository.findByExpiresAtBeforeAndStatus(now, PENDING);

        for (final Reservation reservation : expiredReservations) {
            reservation.setStatus(EXPIRED);
            reservationRepository.save(reservation);
        }
    }
}
