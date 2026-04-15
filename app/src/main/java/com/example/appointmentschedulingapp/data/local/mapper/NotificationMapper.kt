package com.example.appointmentschedulingapp.data.local.mapper

import com.example.appointmentschedulingapp.data.local.entity.NotificationEntity
import com.example.appointmentschedulingapp.domain.model.AppNotification

fun NotificationEntity.toDomain(): AppNotification {
    return AppNotification(
        id = id,
        title = title,
        message = message,
        bookingId = bookingId,
        triggerAt = triggerAt,
        createdAt = createdAt,
        isRead = isRead
    )
}

fun AppNotification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        message = message,
        bookingId = bookingId,
        triggerAt = triggerAt,
        createdAt = createdAt,
        isRead = isRead
    )
}