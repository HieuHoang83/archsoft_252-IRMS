package com.irms.table.controller;

import com.irms.table.domain.Reservation;
import com.irms.table.domain.ReservationStatus;
import com.irms.table.dto.ReservationRequest;
import com.irms.table.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * UC08 — Lấy danh sách đặt bàn.
     * Query param: ?status=CONFIRMED để lọc theo trạng thái.
     */
    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations(
            @RequestParam(required = false) ReservationStatus status) {
        if (status != null) {
            return ResponseEntity.ok(reservationService.getReservationsByStatus(status));
        }
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    /**
     * Lấy đặt bàn trong khoảng thời gian (cho lịch ngày).
     */
    @GetMapping("/between")
    public ResponseEntity<List<Reservation>> getReservationsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(reservationService.getReservationsBetween(from, to));
    }

    /**
     * Lấy thông tin một đặt bàn.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    /**
     * UC08 — Tạo đặt bàn mới.
     */
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody ReservationRequest request) {
        return new ResponseEntity<>(reservationService.createReservation(request), HttpStatus.CREATED);
    }

    /**
     * UC08 — Xác nhận đặt bàn và gán bàn cụ thể.
     * Query param: ?tableId=UUID
     */
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Reservation> confirmReservation(
            @PathVariable UUID id,
            @RequestParam UUID tableId) {
        return ResponseEntity.ok(reservationService.confirmReservation(id, tableId));
    }

    /**
     * UC08 — Hủy đặt bàn.
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }

    /**
     * Đánh dấu khách No-Show.
     */
    @PutMapping("/{id}/no-show")
    public ResponseEntity<Reservation> markNoShow(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.markNoShow(id));
    }
}
