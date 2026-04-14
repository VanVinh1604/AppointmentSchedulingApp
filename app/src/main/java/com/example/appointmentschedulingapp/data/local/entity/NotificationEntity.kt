package com.example.appointmentschedulingapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val bookingId: String?,
    val triggerAt: Long,
    val createdAt: Long,
    val isRead: Boolean = false
)