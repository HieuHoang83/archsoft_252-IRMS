package com.irms.table.domain;

public enum ReservationStatus {
    PENDING,    // Đặt bàn mới, chưa xác nhận bàn cụ thể
    CONFIRMED,  // Đã xác nhận và gán bàn
    SEATED,     // Khách đã ngồi vào bàn
    CANCELLED,  // Đã hủy
    NO_SHOW     // Khách không xuất hiện
}
