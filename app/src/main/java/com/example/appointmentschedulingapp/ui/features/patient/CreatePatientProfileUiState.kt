package com.example.appointmentschedulingapp.ui.features.patient

import com.example.appointmentschedulingapp.domain.model.location.Province
import com.example.appointmentschedulingapp.domain.model.location.Ward

data class CreatePatientProfileUiState(
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "Nam",
    val phoneNumber: String = "",
    val addressDetail: String = "",
    val provinces: List<Province> = emptyList(),
    val wards: List<Ward> = emptyList(),
    val selectedProvince: Province? = null,
    val selectedWard: Ward? = null,

    val isLoadingLocation: Boolean = false,
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