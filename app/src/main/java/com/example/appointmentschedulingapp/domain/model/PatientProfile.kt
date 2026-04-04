package com.example.appointmentschedulingapp.domain.model

data class PatientProfile(
    val id: String, // Mã số y tế [cite: 230]
    val fullName: String,
    val dob: String, // Ngày sinh
    val gender: String,
    val address: String,
    val identityCard: String, // Số CCCD [cite: 230]
    val healthInsurance: String? = null // BHYT (có thể có hoặc không)
)