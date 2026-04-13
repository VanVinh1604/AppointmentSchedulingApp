package com.example.appointmentschedulingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_profiles")
data class PatientProfileEntity(
    @PrimaryKey val id: String = "",
    val userId: String = "",           // để filter theo user
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val phoneNumber: String = "",
    val provinceCode: Int,
    val provinceName: String,
    val wardCode: Int,
    val wardName: String,
    val addressDetail: String,
    val identityCard: String = "",
    val healthInsuranceNumber: String = "",
    val healthInsuranceExpiry: String = "",
    val relationship: String = "",
    val emergencyContact: String = "",
    val allergies: String = "",
    val medicalHistory: String = "",
    val isDefault: Boolean = false
)