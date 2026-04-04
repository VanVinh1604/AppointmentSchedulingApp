package com.example.appointmentschedulingapp.domain.model

data class Clinic(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val district: String = "",
    val city: String = "",

    val imageUrl: String = "",
    val bannerUrl: String = "",

    val rating: Double = 0.0,      // Nên để 0.0 ban đầu
    val reviewsCount: Int = 0,     // Nên để 0 ban đầu

    val type: String = "",         // Ví dụ: hospital, clinic, dental
    val specialties: List<String> = emptyList(),

    val description: String = "",
    val services: List<String> = emptyList(),

    val openTime: String = "",     // Định dạng "06:00"
    val closeTime: String = "",    // Định dạng "17:00"
    val isOpen24Hours: Boolean = false,

    val consultationFee: Long = 0L, // Dùng Long cho tiền tệ (VND) sẽ chuẩn hơn
    val emergencySupport: Boolean = false,

    val latitude: Double = 0.0,
    val longitude: Double = 0.0,

    val phoneNumber: String = "",
    val website: String = "",

    val insuranceSupported: Boolean = false,
    val parkingAvailable: Boolean = false,

    val totalDoctors: Int = 0,

    val availableRooms: List<String> = emptyList(),
    val availableDates: List<String> = emptyList(),
    val availableTimes: List<String> = emptyList()
)