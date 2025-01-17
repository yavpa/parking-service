package com.gesund.parking_system.common.mappers;

import com.gesund.parking_system.common.domain.ParkingSlot;
import com.gesund.parking_system.common.dto.ParkingSlotView;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-17T13:32:50+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class ParkingSlotMapperImpl implements ParkingSlotMapper {

    @Override
    public ParkingSlotView toParkingSlotView(ParkingSlot parkingSlot) {
        if ( parkingSlot == null ) {
            return null;
        }

        String carId = null;
        LocalDateTime checkIn = null;
        LocalDateTime checkOut = null;

        carId = parkingSlot.getCarId();
        checkIn = parkingSlot.getCheckIn();
        checkOut = parkingSlot.getCheckOut();

        ParkingSlotView parkingSlotView = new ParkingSlotView( carId, checkIn, checkOut );

        return parkingSlotView;
    }
}
