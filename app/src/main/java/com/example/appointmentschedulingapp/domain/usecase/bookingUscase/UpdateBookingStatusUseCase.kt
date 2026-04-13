package com.example.appointmentschedulingapp.domain.usecase.bookingUscase

import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus
import javax.inject.Inject

class UpdateBookingStatusUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(bookingId: String, status: BookingStatus): Result<Unit> =
        repository.updateBookingStatus(bookingId, status)
}