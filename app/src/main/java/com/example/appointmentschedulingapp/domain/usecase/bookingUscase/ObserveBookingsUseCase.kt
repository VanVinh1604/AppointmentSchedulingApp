package com.example.appointmentschedulingapp.domain.usecase.bookingUscase

import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    operator fun invoke(): Flow<List<Booking>> = repository.observeBookings()
}