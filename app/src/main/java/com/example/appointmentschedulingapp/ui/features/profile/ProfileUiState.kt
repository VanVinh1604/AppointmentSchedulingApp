package com.example.appointmentschedulingapp.ui.features.profile

import com.example.appointmentschedulingapp.domain.model.PatientProfile

data class ProfileUiState(
    val profiles: List<PatientProfile> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)