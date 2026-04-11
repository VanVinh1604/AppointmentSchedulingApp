package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.Booking
import kotlinx.coroutines.flow.Flow

interface BookingRepository {

    // Returns booking ID on success
    suspend fun createBooking(
        booking: Booking
    ): Result<String>


    /** Fetches all bookings for the currently authenticated user */
    suspend fun getBookings(): Result<List<Booking>>

    /** Fetches a single booking by ID */
    suspend fun getBookingById(bookingId: String): Result<Booking>

    /** Cancels a booking by ID */
    suspend fun cancelBooking(bookingId: String): Result<Unit>

    /** Observes bookings as a live stream (optional – for real-time updates) */
    fun observeBookings(): Flow<List<Booking>>
}