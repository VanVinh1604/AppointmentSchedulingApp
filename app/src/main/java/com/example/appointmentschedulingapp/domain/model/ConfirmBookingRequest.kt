package com.example.appointmentschedulingapp.domain.model

data class ConfirmBookingRequest(
    val clinicId: String,
    val clinicName: String,
    val clinicAddress: String,
    val consultationFee: Long,

    val patientId: String,
    val patientName: String,

    val specialty: String,
    val service: String,

    val appointmentDate: String,
    val appointmentTime: String,

    val existingBookingId: String? = null,
    val paymentMethod: String
)