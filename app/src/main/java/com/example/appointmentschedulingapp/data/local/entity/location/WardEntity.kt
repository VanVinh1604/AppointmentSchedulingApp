package com.example.appointmentschedulingapp.data.local.entity.location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wards")
data class WardEntity(
    @PrimaryKey
    val code: Int,
    val provinceCode: Int,
    val name: String
)