package com.example.appointmentschedulingapp.domain.model

data class Booking(
    val id: String = "",
    val clinicId: String = "",
    val clinicName: String = "",

    val departmentId: String = "",
    val doctorId: String = "",

    val patientId: String = "",
    val patientName: String = "",

    val specialty: String = "",
    val service: String = "",

    val slotId: String = "",
    val appointmentDate: String = "",
    val appointmentTime: String = "",

    val status: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis()
)