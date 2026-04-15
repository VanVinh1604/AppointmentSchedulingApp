package com.example.appointmentschedulingapp.domain.model

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val bookingId: String?,
    val triggerAt: Long,
    val createdAt: Long,
    val isRead: Boolean
)