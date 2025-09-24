package com.data.repository;

import com.data.entity.Pricing;
import com.data.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {

    List<Pricing> findByParkingLot_IdAndVehicleType(Long lotId, VehicleType vehicleType);

    List<Pricing> findByParkingLot_IdAndVehicleTypeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Long lotId,
            VehicleType vehicleType,
            LocalDateTime start,
            LocalDateTime end
    );
}
