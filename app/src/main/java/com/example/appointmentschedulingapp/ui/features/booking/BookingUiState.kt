package com.example.appointmentschedulingapp.ui.features.booking

// BookingUiState.kt
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.model.Doctor

data class BookingUiState(
    val bookingId: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,

    val selectedBookingType: String = "",
    val selectedClinic: Clinic? = null,
    val selectedSpecialty: String = "",

    val doctors: List<Doctor> = emptyList(),
    val selectedDoctor: Doctor? = null,

    val selectedDate: String = "",
    val selectedTime: String = "",

    val selectedPatientId: String = "",
    val patientName: String = "",

    val currentStep: Int = 1
)