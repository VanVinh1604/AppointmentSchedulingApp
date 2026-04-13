package com.example.appointmentschedulingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey val id: String = "",
    val departmentId: String = "",
    val clinicId: String = "",         // để filter theo clinic
    val fullName: String = "",
    val title: String = "",
    val rating: Double = 0.0,
    val imageUrl: String = "",
    val biography: String = ""
)