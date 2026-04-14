package com.example.appointmentschedulingapp.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncPendingBookingsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val bookingRepository: BookingRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            bookingRepository.syncPendingBookings()
                .fold(
                    onSuccess = { Result.success() },
                    onFailure = { Result.retry() }
                )
        } catch (e: Exception) {
            Result.retry()
        }
    }
}