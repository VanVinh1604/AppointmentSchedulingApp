package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.AppNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun observeNotifications(): Flow<List<AppNotification>>
    suspend fun insertNotification(notification: AppNotification)
    suspend fun markAllAsRead()
}