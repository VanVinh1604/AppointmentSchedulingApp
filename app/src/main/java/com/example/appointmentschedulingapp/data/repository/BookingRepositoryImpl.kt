package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BookingRepository {


    override suspend fun createBooking(
        booking: Booking
    ): Result<String> =
        withContext(dispatcher){
         try {
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