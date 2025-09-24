package com.data.repository;

import com.data.entity.ParkingLot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    @Query("SELECT DISTINCT p FROM ParkingLot p " +
            "LEFT JOIN p.pricings pr " +
            "LEFT JOIN p.reviews r " +
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:city IS NULL OR p.city = :city) " +
            "AND (:ward IS NULL OR p.ward = :ward) " +
            "AND (:minPrice IS NULL OR pr.pricePerHour >= :minPrice) " +
            "AND (:maxPrice IS NULL OR pr.pricePerHour <= :maxPrice) " +
            "AND (:minSlots IS NULL OR p.availableSlots >= :minSlots) " +
            "GROUP BY p " +
            "HAVING (:minRating IS NULL OR COALESCE(AVG(r.rating), 0) >= :minRating)")
    Page<ParkingLot> findByFilters(
            @Param("name") String name,
            @Param("city") String city,
            @Param("ward") String ward,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minRating") Double minRating,
            @Param("minSlots") Integer minSlots,
            Pageable pageable
    );

    Page<ParkingLot> findByOwnerId(Long ownerId, Pageable pageable);
}
