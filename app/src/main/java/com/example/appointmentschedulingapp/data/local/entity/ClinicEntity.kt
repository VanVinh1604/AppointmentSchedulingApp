package com.example.appointmentschedulingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinics")
data class ClinicEntity(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val specialty: String = "",
    val imageUrl: String = ""
)