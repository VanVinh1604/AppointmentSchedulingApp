package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import javax.inject.Inject

class GetBookingByIdUseCase @Inject constructor(private val repo: BookingRepository) {
    suspend operator fun invoke(bookingId: String) = repo.getBookingById(bookingId)
}