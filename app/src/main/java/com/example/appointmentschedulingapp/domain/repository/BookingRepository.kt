package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.Booking

interface BookingRepository {

    // Returns booking ID on success
    suspend fun createBooking(
        booking: Booking
    ): Result<String>
}