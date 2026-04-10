package com.example.appointmentschedulingapp.ui.features.patient

data class CreatePatientProfileUiState(
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "Nam",
    val phoneNumber: String = "",
    val address: String = "",

    val identityCard: String = "",
    val healthInsuranceNumber: String = "",
    val healthInsuranceExpiry: String = "",

    val relationship: String = "Bản thân",
    val emergencyContact: String = "",

    val allergies: String = "",
    val medicalHistory: String = "",

    val isDefault: Boolean = false,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)