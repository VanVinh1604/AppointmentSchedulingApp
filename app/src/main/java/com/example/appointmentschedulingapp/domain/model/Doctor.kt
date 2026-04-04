package com.example.appointmentschedulingapp.domain.model

data class Doctor(
    val id: String,
    val departmentId: String,
    val fullName: String,
    val title: String, // Ví dụ: Thạc sĩ, Bác sĩ CKI
    val rating: Double
)