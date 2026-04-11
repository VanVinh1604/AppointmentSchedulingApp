package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import javax.inject.Inject

class CancelBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(bookingId: String): Result<Unit> =
        repository.cancelBooking(bookingId)
}