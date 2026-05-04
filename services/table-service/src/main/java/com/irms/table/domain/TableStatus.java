package com.irms.table.domain;

public enum TableStatus {
    AVAILABLE,   // Bàn trống, sẵn sàng xếp khách
    OCCUPIED,    // Bàn đang có khách
    RESERVED,    // Bàn đã được đặt trước
    CLEANING     // Bàn đang được dọn dẹp
}
