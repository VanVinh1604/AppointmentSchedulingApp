package com.example.appointmentschedulingapp.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.appointmentschedulingapp.core.utils.NotificationHelper
import com.example.appointmentschedulingapp.data.local.dao.NotificationDao
import com.example.appointmentschedulingapp.data.local.entity.NotificationEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.UUID

@HiltWorker
class AppointmentReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationDao: NotificationDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val bookingId = inputData.getString("bookingId")
            ?: return Result.failure()

        val clinicName = inputData.getString("clinicName")
            ?: "Phòng khám"

        val title = "Nhắc lịch khám"
        val patientName = inputData.getString("patientName") ?: "bạn"
        val message = "$patientName có lịch khám sắp tới tại $clinicName"
        Log.d("ReminderWorker", "Reminder fired for $bookingId")
        NotificationHelper.show(
            context = applicationContext,
            title = title,
            message = message
        )

        notificationDao.insert(
            NotificationEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                message = message,
                bookingId = bookingId,
                triggerAt = System.currentTimeMillis(),
                createdAt = System.currentTimeMillis()
            )
        )

        return Result.success()
    }
}