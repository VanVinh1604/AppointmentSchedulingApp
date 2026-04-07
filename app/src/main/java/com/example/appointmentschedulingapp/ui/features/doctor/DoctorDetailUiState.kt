package com.example.appointmentschedulingapp.ui.features.doctor

import com.example.appointmentschedulingapp.domain.model.Doctor

data class DoctorDetailUiState(
    val isLoading: Boolean = false,
    val doctor: Doctor? = null,
    val error: String? = null
)