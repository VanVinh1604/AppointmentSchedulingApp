package com.example.appointmentschedulingapp.domain.model

data class PatientProfile(
    val id: String = "",
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val phoneNumber: String = "",
//    val address: String = "",
    val provinceCode: Int = 0,
    val provinceName: String = "",
    val wardCode: Int = 0,
    val wardName: String = "",
    val addressDetail: String = "",

    val identityCard: String = "",
    val healthInsuranceNumber: String = "",
    val healthInsuranceExpiry: String = "",

    val relationship: String = "Bản thân", // Bản thân / Con / Cha / Mẹ
    val emergencyContact: String = "",

    val allergies: String = "",
    val medicalHistory: String = "",

    val qrCodeUrl: String = "",
    val avatarUrl: String = "",

    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
){
    val fullAddress: String
        get() = listOf(
            addressDetail,
            wardName,
            provinceName
        ).filter { it.isNotBlank() }
            .joinToString(", ")
}