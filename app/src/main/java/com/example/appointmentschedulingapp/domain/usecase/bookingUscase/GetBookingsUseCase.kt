package com.example.appointmentschedulingapp.domain.usecase.bookingUscase

import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import javax.inject.Inject

class GetBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(): Result<List<Booking>> = repository.getBookings()

    suspend operator fun invoke(bookingId: String): Result<Booking> =
        repository.getBookingById(bookingId)

    
}