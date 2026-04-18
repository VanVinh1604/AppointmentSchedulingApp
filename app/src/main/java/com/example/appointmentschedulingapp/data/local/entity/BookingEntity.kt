package com.example.appointmentschedulingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String = "",
    val userId: String = "",           // để filter theo user
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
    val status: String = BookingStatus.PENDING_PAYMENT.name,
    val createdAt: Long = System.currentTimeMillis(),

    val synced: Boolean = false
)