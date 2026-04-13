package com.example.appointmentschedulingapp.ui.features.profile

import com.example.appointmentschedulingapp.domain.model.PatientProfile

data class ProfileDetailUiState(
    val profile: PatientProfile? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isEditMode: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,

    // Edit fields — mirror PatientProfile
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val identityCard: String = "",
    val healthInsuranceNumber: String = "",
    val healthInsuranceExpiry: String = "",
    val relationship: String = "",
    val emergencyContact: String = "",
    val allergies: String = "",
    val medicalHistory: String = "",
    val isDefault: Boolean = false
)