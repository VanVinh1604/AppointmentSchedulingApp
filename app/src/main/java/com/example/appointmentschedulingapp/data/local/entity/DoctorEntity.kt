package com.example.appointmentschedulingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey
    val id: String = "",
    val clinicId: String = "",
    val name: String = "",
    val specialty: String = "",
    val experience: Int = 0,
    val imageUrl: String = ""
)