package com.example.appointmentschedulingapp.data.local.entity.location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "provinces")
data class ProvinceEntity(
    @PrimaryKey
    val code: Int,
    val name: String
)