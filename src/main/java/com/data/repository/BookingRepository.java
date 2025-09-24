package com.data.repository;

import com.data.entity.Booking;
import com.data.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByUser_Id(Long userId, Pageable pageable);

    List<Booking> findByParkingLot_Id(Long lotId);

    List<Booking> findByBookingStatus(BookingStatus status);

    List<Booking> findByExpireAtBeforeAndBookingStatus(LocalDateTime now, BookingStatus status);

    List<Booking> findByBookingStatusAndExpireAtBefore(BookingStatus status, LocalDateTime now);

    @Query("""
        SELECT b FROM Booking b
        WHERE (:username IS NULL OR b.user.username LIKE %:username%)
        AND (:status IS NULL OR b.bookingStatus = :status)
        AND (:lotId IS NULL OR b.parkingLot.id = :lotId)
    """)
    Page<Booking> searchBookings(
            @Param("username") String username,
            @Param("status") BookingStatus status,
            @Param("lotId") Long lotId,
            Pageable pageable
    );

    @Query("""
    SELECT b FROM Booking b
    WHERE b.parkingLot.owner.id = :ownerId
    AND (:status IS NULL OR b.bookingStatus = :status)
""")
    Page<Booking> findByParkingLot_Owner_IdAndStatus(
            @Param("ownerId") Long ownerId,
            @Param("status") BookingStatus status,
            Pageable pageable
    );

    @Query("SELECT b FROM Booking b WHERE b.vehicle.id = :vehicleId " +
            "AND b.startTime <= :now AND b.endTime >= :now " +
            "AND b.bookingStatus = 'CONFIRMED'")
    Optional<Booking> findActiveBookingForVehicle(@Param("vehicleId") Long vehicleId,
                                                  @Param("now") LocalDateTime now);
}
