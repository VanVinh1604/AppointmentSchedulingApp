package com.example.appointmentschedulingapp.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PaymentRetryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val bookingRepository: BookingRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val bookingId = inputData.getString("booking_id")
            ?: return Result.failure()

        Log.d("PaymentRetry", "Checking booking $bookingId")

        return try {
            val bookingResult = bookingRepository.getBookingById(bookingId)

            bookingResult.fold(
                onSuccess = { booking ->
                    when (booking.status) {
                        BookingStatus.PAID,
                        BookingStatus.CONFIRMED,
                        BookingStatus.COMPLETED,
                        BookingStatus.FAILED,
                        BookingStatus.CANCELLED -> {
                            Log.d("PaymentRetry", "Booking $bookingId already resolved: ${booking.status}")
                            Result.success()  // Kết thúc worker, không làm gì
                        }

                        // ✅ Vẫn pending sau 2 phút → timeout, set FAILED
                        BookingStatus.PENDING_PAYMENT -> {
                            Log.d("PaymentRetry", "Booking $bookingId still pending → set FAILED")
                            bookingRepository.updateBookingStatus(
                                bookingId,
                                BookingStatus.FAILED
                            )
                            Result.success()
                        }

                        else -> Result.success()
                    }
                },
                onFailure = {
                    Log.e("PaymentRetry", "Failed to get booking $bookingId: ${it.message}")
                    Result.retry()
                }
            )
        } catch (e: Exception) {
            Log.e("PaymentRetry", "Exception for $bookingId: ${e.message}")
            Result.retry()
        }
    }
}