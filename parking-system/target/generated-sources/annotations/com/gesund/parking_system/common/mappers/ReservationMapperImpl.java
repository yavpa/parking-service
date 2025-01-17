package com.gesund.parking_system.common.mappers;

import com.gesund.parking_system.common.ReservationStatus;
import com.gesund.parking_system.common.domain.Reservation;
import com.gesund.parking_system.common.dto.ReservationView;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-17T13:32:50+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public ReservationView toReservationView(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        String carId = null;
        LocalDateTime reservedAt = null;
        LocalDateTime expiresAt = null;
        ReservationStatus status = null;

        carId = reservation.getCarId();
        reservedAt = reservation.getReservedAt();
        expiresAt = reservation.getExpiresAt();
        status = reservation.getStatus();

        ReservationView reservationView = new ReservationView( carId, reservedAt, expiresAt, status );

        return reservationView;
    }
}
