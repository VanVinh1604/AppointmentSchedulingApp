package com.example.appointmentschedulingapp.domain.usecase.notification

import com.example.appointmentschedulingapp.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkAllAsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke() {
        repository.markAllAsRead()
    }
}