package com.example.appointmentschedulingapp.ui.features.tickets

enum class BookingStatus(val label: String) {
    PENDING("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    UNPAID("Chưa thanh toán"),
    PAID("Đã thanh toán"),
    COMPLETED("Đã khám"),
    CANCELLED("Đã hủy")
}