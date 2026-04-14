package com.example.appointmentschedulingapp.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PaymentRetryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val bookingId = inputData.getString("booking_id") ?: return Result.failure()
        // TODO: inject PaymentRepository và thực hiện retry
        return Result.success()
    }
}