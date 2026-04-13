package com.example.appointmentschedulingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinics")
data class ClinicEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val address: String = "",
    val district: String = "",
    val city: String = "",
    val imageUrl: String = "",
    val bannerUrl: String = "",
    val rating: Double = 0.0,
    val reviewsCount: Int = 0,
    val type: String = "",
    val specialties: String = "",      // List<String> → JSON string
    val description: String = "",
    val services: String = "",         // List<String> → JSON string
    val openTime: String = "",
    val closeTime: String = "",
    val isOpen24Hours: Boolean = false,
    val consultationFee: Long = 0L,
    val emergencySupport: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val phoneNumber: String = "",
    val website: String = "",
    val insuranceSupported: Boolean = false,
    val parkingAvailable: Boolean = false,
    val totalDoctors: Int = 0
)