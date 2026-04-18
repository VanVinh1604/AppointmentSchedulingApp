package com.example.appointmentschedulingapp.ui.features.tickets

enum class BookingStatus(val label: String) {
    PENDING_PAYMENT("Chờ thanh toán"),
    PAID("Đã thanh toán"),
    CONFIRMED("Đã xác nhận"),
    COMPLETED("Đã khám"),
    FAILED("Thanh toán thất bại"),
    CANCELLED("Đã hủy")
}