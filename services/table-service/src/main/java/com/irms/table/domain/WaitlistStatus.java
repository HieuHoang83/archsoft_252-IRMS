package com.irms.table.domain;

public enum WaitlistStatus {
    WAITING,   // Đang trong hàng đợi
    NOTIFIED,  // Đã được thông báo bàn sẵn sàng (đang đếm ngược)
    SEATED,    // Đã được xếp vào bàn
    LEFT       // Khách đã rời đi / hủy chờ
}
