package com.gesund.parking_system.common.mappers;

import com.gesund.parking_system.common.domain.ParkingSlot;
import com.gesund.parking_system.common.dto.ParkingSlotView;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParkingSlotMapper {
    ParkingSlotView toParkingSlotView(ParkingSlot parkingSlot);
}
