package com.data.repository;

import com.data.entity.VehicleEntryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleEntryLogRepository extends JpaRepository<VehicleEntryLog, Long> {
}
