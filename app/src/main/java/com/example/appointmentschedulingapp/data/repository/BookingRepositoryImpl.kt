package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val auth: FirebaseAuth,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BookingRepository {

    private fun requireUid(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")
    }

    private fun bookingRef(bookingId: String) =
        firebaseDatabase.getReference(Config.FIREBASE_BOOKINGS).child(bookingId)

    private fun userBookingsRef(uid: String) =
        firebaseDatabase.getReference(Config.FIREBASE_USER_BOOKINGS).child(uid)

    override suspend fun createBooking(booking: Booking): Result<String> =
        withContext(dispatcher) {
            runCatching {
                val uid = requireUid()

                val ref = firebaseDatabase
                    .getReference(Config.FIREBASE_BOOKINGS)
                    .push()

                val bookingId = ref.key
                    ?: error("Firebase push() returned null key")

                val bookingWithId = booking.copy(id = bookingId)

                val updates = hashMapOf<String, Any>(
                    "${Config.FIREBASE_BOOKINGS}/$bookingId" to bookingWithId,
                    "${Config.FIREBASE_USER_BOOKINGS}/$uid/$bookingId" to true
                )

                firebaseDatabase.reference.updateChildren(updates).await()
                bookingId
            }
        }

    override suspend fun getBookings(): Result<List<Booking>> =
        withContext(dispatcher) {
            runCatching {
                val uid = requireUid()

                val indexSnapshot = userBookingsRef(uid).get().await()

                indexSnapshot.children
                    .mapNotNull { it.key }
                    .mapNotNull { id ->
                        bookingRef(id).get().await().toBooking()
                    }
                    .sortedByDescending { it.createdAt }
            }
        }

    override suspend fun getBookingById(bookingId: String): Result<Booking> =
        withContext(dispatcher) {
            runCatching {
                bookingRef(bookingId).get().await().toBooking()
                    ?: throw NoSuchElementException("Booking not found")
            }
        }

    override suspend fun cancelBooking(bookingId: String): Result<Unit> =
        withContext(dispatcher) {
            runCatching {
                bookingRef(bookingId)
                    .child("status")
                    .setValue(BookingStatus.CANCELLED.name)
                    .await()

                Unit
            }
        }

    override fun observeBookings(): Flow<List<Booking>> = callbackFlow {
        val uid = requireUid()
        val indexRef = userBookingsRef(uid)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookingIds = snapshot.children.mapNotNull { it.key }

                if (bookingIds.isEmpty()) {
                    trySend(emptyList())
                    return
                }

                val bookingsRef = firebaseDatabase
                    .getReference(Config.FIREBASE_BOOKINGS)

                bookingsRef.get()
                    .addOnSuccessListener { allBookingsSnapshot ->
                        val bookings = bookingIds.mapNotNull { id ->
                            allBookingsSnapshot.child(id).toBooking()
                        }.sortedByDescending { it.createdAt }

                        trySend(bookings)
                    }
                    .addOnFailureListener {
                        trySend(emptyList())
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        indexRef.addValueEventListener(listener)
        awaitClose { indexRef.removeEventListener(listener) }
    }

    override suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Result<Unit> =
        withContext(dispatcher) {
            runCatching {
                bookingRef(bookingId)
                    .child("status")
                    .setValue(status.name)
                    .await()
                Unit
            }
        }

    private fun DataSnapshot.toBooking(): Booking? = runCatching {
        Booking(
            id = child("id").getValue(String::class.java) ?: key ?: "",
            clinicId = child("clinicId").getValue(String::class.java) ?: "",
            clinicName = child("clinicName").getValue(String::class.java) ?: "",
            clinicAddress = child("clinicAddress").getValue(String::class.java) ?: "",
            departmentId = child("departmentId").getValue(String::class.java) ?: "",
            doctorId = child("doctorId").getValue(String::class.java) ?: "",
            patientId = child("patientId").getValue(String::class.java) ?: "",
            patientName = child("patientName").getValue(String::class.java) ?: "",
            patientPhone = child("patientPhone").getValue(String::class.java) ?: "",
            specialty = child("specialty").getValue(String::class.java) ?: "",
            service = child("service").getValue(String::class.java) ?: "",
            slotId = child("slotId").getValue(String::class.java) ?: "",
            appointmentDate = child("appointmentDate").getValue(String::class.java) ?: "",
            appointmentTime = child("appointmentTime").getValue(String::class.java) ?: "",
            consultationFee = child("consultationFee").getValue(Long::class.java) ?: 0L,
            paymentMethod = child("paymentMethod").getValue(String::class.java) ?: "",
            status = runCatching {
                BookingStatus.valueOf(
                    child("status").getValue(String::class.java) ?: ""
                )
            }.getOrDefault(BookingStatus.PENDING),
            createdAt = child("createdAt").getValue(Long::class.java)
                ?: System.currentTimeMillis()
        )
    }.getOrNull()
}