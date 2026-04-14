package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.AppNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun observeNotifications(): Flow<List<AppNotification>>
    suspend fun saveNotification(notification: AppNotification)
    suspend fun markAsRead(id: String)
    suspend fun markAllAsRead()
    suspend fun scheduleAppointmentReminder(
        bookingId: String,
        clinicName: String,
        appointmentTimeMillis: Long
    )
}