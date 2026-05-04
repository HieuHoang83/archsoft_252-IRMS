package com.irms.table.repository;

import com.irms.table.domain.Reservation;
import com.irms.table.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByReservationTimeBetween(LocalDateTime from, LocalDateTime to);

    List<Reservation> findByTableIdAndStatus(UUID tableId, ReservationStatus status);

    List<Reservation> findByStatusOrderByReservationTimeAsc(ReservationStatus status);

    @Query(value = "SELECT * FROM reservations r " +
                   "WHERE r.table_id = :tableId " +
                   "AND r.status IN ('PENDING', 'CONFIRMED', 'SEATED') " +
                   "AND r.reservation_time < :endTime " +
                   "AND :startTime < (r.reservation_time + (r.expected_duration_minutes * INTERVAL '1 minute'))", 
           nativeQuery = true)
    List<Reservation> findOverlappingReservations(
            @Param("tableId") UUID tableId, 
            @Param("startTime") LocalDateTime startTime, 
            @Param("endTime") LocalDateTime endTime);
}
