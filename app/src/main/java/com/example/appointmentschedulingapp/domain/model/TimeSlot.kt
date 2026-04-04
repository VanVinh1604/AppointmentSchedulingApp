package com.example.appointmentschedulingapp.domain.model

import com.example.appointmentschedulingapp.domain.enum.Session

data class TimeSlot(
    val id: String = "",
    val doctorId: String = "",
    val clinicId: String = "",
    val departmentId: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val session: Session = Session.MORNING,
    val isBooked: Boolean = false
)