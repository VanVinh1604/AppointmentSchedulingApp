package com.example.appointmentschedulingapp.domain.model

data class User(
    val id: String,
    val phoneNumber: String,
    val languagePreference: String // Lưu lựa chọn ngôn ngữ từ Splash Screen [cite: 13, 84]
)