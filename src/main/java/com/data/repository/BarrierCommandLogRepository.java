package com.data.repository;

import com.data.entity.BarrierCommandLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarrierCommandLogRepository extends JpaRepository<BarrierCommandLog, Long> {
}
