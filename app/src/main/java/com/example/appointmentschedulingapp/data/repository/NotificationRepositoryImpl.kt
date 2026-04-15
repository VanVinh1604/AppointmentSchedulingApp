package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.data.local.dao.NotificationDao
import com.example.appointmentschedulingapp.data.local.mapper.toDomain
import com.example.appointmentschedulingapp.data.local.mapper.toEntity
import com.example.appointmentschedulingapp.domain.model.AppNotification
import com.example.appointmentschedulingapp.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val dao: NotificationDao
) : NotificationRepository {

    override fun observeNotifications(): Flow<List<AppNotification>> {
        return dao.observeNotifications()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun insertNotification(notification: AppNotification) {
        dao.insert(notification.toEntity())
    }

    override suspend fun markAllAsRead() {
        dao.markAllAsRead()
    }


}