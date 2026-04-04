package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : BookingRepository {


    override suspend fun createBooking(
        booking: Booking
    ): Result<String> {
        return try {
            val ref = firebaseDatabase
                .getReference(Config.FIREBASE_BOOKINGS)
                .push()

            val bookingId = ref.key ?: ""

            ref.setValue(
                booking.copy(id = bookingId)
            ).await()

            Result.success(bookingId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}