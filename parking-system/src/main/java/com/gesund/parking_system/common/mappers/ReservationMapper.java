package com.gesund.parking_system.common.mappers;

import com.gesund.parking_system.common.domain.Reservation;
import com.gesund.parking_system.common.dto.ReservationView;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    ReservationView toReservationView(Reservation reservation);
}
