package com.example.appointmentschedulingapp.di

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.appointmentschedulingapp.workers.AppointmentReminderWorker
import com.example.appointmentschedulingapp.workers.PaymentRetryWorker
import com.example.appointmentschedulingapp.workers.SyncPendingBookingsWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

// di/WorkScheduler.kt
@Singleton
class WorkScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    fun scheduleSyncPendingBookings() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncPendingBookingsWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork(
            "sync_pending_bookings",
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    fun schedulePaymentRetry(bookingId: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val data = workDataOf("booking_id" to bookingId)

        val request = OneTimeWorkRequestBuilder<PaymentRetryWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniqueWork(
            "payment_retry_$bookingId",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun scheduleAppointmentReminder(appointmentId: String, delayMinutes: Long) {
        val request = OneTimeWorkRequestBuilder<AppointmentReminderWorker>()
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .setInputData(workDataOf("appointment_id" to appointmentId))
            .build()

        workManager.enqueueUniqueWork(
            "reminder_$appointmentId",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}