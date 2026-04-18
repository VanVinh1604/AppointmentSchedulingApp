package com.example.appointmentschedulingapp.domain.model

import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus

data class Booking(
    val id: String = "",
    val clinicId: String = "",
    val clinicName: String = "",
    val clinicAddress: String = "",
    val clinicDistrict: String = "",
    val clinicCity: String = "",
    val insuranceSupported: Boolean = false,

    val departmentId: String = "",
    val doctorId: String = "",
    val doctorName: String = "",

    val patientId: String = "",
    val patientName: String = "",
    val patientPhone: String = "",
    val patientDateOfBirth: String = "",
    val patientHealthInsurance: String = "",

    val specialty: String = "",
    val service: String = "",
    val bookingType: String = "",

    val slotId: String = "",
    val appointmentDate: String = "",
    val appointmentTime: String = "",

    val consultationFee: Long = 0L,
    val paymentMethod: String = "",

    val status: BookingStatus = BookingStatus.PENDING_PAYMENT,
    val createdAt: Long = System.currentTimeMillis()
)