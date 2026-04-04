package com.example.appointmentschedulingapp.domain.model

data class Appointment(
    val id: String,
    val doctorId: String,
    val timeSlotId: String,
    val totalPrice: Double,
    val qrCodeData: String, // Dữ liệu để tạo mã QR phiếu khám [cite: 226, 228]
    val creatorId: String, // ID người đặt
    val patientProfileId: String, // ID bệnh nhân đi khám
    val status: String // "Pending", "Confirmed" [cite: 214, 222]
)