package com.example.appointmentschedulingapp.domain.usecase.notification

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.appointmentschedulingapp.workers.AppointmentReminderWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleReminderUseCase @Inject constructor(
    private val workManager: WorkManager
) {
    operator fun invoke(
        bookingId: String,
        clinicName: String,
        patientName: String,
        appointmentTimeMillis: Long
    ) {
        val remindBefore = 24 * 60 * 60 * 1000L

        val triggerAt = appointmentTimeMillis - remindBefore
        val delay = (triggerAt - System.currentTimeMillis())
            .coerceAtLeast(5_000L)

        val request = OneTimeWorkRequestBuilder<AppointmentReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "bookingId" to bookingId,
                    "patientName" to patientName,
                    "clinicName" to clinicName
                )
            )
            .build()

        workManager.enqueueUniqueWork(
            "booking_reminder_$bookingId",
            ExistingWorkPolicy.REPLACE,
            request
        )


    }
}