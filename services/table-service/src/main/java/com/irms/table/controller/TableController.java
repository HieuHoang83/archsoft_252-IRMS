package com.irms.table.controller;

import com.irms.table.domain.RestaurantTable;
import com.irms.table.domain.TableStatus;
import com.irms.table.dto.SeatGuestRequest;
import com.irms.table.dto.TableRequest;
import com.irms.table.dto.TableStatusUpdateRequest;
import com.irms.table.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    /**
     * UC01 — Lấy tất cả bàn để hiển thị sơ đồ Floor Plan.
     * Query param: ?status=AVAILABLE để lọc theo trạng thái.
     */
    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getTables(
            @RequestParam(required = false) TableStatus status) {
        if (status != null) {
            return ResponseEntity.ok(tableService.getTablesByStatus(status));
        }
        return ResponseEntity.ok(tableService.getAllTables());
    }

    /**
     * UC10 — Tìm bàn AVAILABLE phù hợp với số lượng khách.
     */
    @GetMapping("/available")
    public ResponseEntity<List<RestaurantTable>> getAvailableTables(
            @RequestParam(defaultValue = "1") Integer partySize) {
        return ResponseEntity.ok(tableService.getAvailableTablesForParty(partySize));
    }

    /**
     * Lấy thông tin một bàn theo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTable> getTableById(@PathVariable UUID id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    /**
     * Tạo bàn mới (Admin).
     */
    @PostMapping
    public ResponseEntity<RestaurantTable> createTable(@Valid @RequestBody TableRequest request) {
        return new ResponseEntity<>(tableService.createTable(request), HttpStatus.CREATED);
    }

    /**
     * Cập nhật trạng thái bàn (UC10 — xếp bàn, dọn bàn...).
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<RestaurantTable> updateTableStatus(
            @PathVariable UUID id,
            @Valid @RequestBody TableStatusUpdateRequest request) {
        return ResponseEntity.ok(tableService.updateTableStatus(id, request));
    }

    /**
     * UC10 — Xếp bàn cho khách (từ RESERVATION, WAITLIST hoặc WALK_IN).
     */
    @PostMapping("/seat")
    public ResponseEntity<RestaurantTable> seatGuest(@Valid @RequestBody SeatGuestRequest request) {
        return ResponseEntity.ok(tableService.seatGuest(request));
    }

    @PutMapping("/{fromTableId}/move/{toTableId}")
    public ResponseEntity<RestaurantTable> moveTable(
            @PathVariable UUID fromTableId,
            @PathVariable UUID toTableId) {
        return ResponseEntity.ok(tableService.moveTable(fromTableId, toTableId));
    }
}
