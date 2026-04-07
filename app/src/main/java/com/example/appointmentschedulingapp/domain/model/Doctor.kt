package com.example.appointmentschedulingapp.domain.model

data class Doctor(
    val id: String = "",           // Gán giá trị mặc định là chuỗi rỗng
    val departmentId: String = "",  // Gán giá trị mặc định
    val fullName: String = "",      // Gán giá trị mặc định
    val title: String = "",         // Gán giá trị mặc định
    val rating: Double = 0.0,       // Gán giá trị mặc định là 0.0
    val imageUrl: String = "",      // Gán giá trị mặc định
    val biography: String = ""      // Gán giá trị mặc định
)