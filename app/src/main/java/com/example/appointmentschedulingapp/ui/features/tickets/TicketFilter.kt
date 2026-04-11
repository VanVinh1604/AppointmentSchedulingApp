package com.example.appointmentschedulingapp.ui.features.tickets

enum class TicketFilter(val label: String) {
    ALL("Tất cả"),
    UNPAID("Chưa thanh toán"),
    PAID("Đã thanh toán"),
    COMPLETED("Đã khám"),
    CANCELLED("Đã hủy")
}