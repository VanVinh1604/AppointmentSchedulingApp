package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.data.local.dao.BookingDao
import com.example.appointmentschedulingapp.data.local.mapper.toDomain
import com.example.appointmentschedulingapp.data.local.mapper.toEntity
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val bookingDao: BookingDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BookingRepository {

    private fun requireUid() = auth.currentUser?.uid
        ?: throw IllegalStateException("User not authenticated")

    private fun bookingRef(bookingId: String) =
        firebaseDatabase.getReference(Config.FIREBASE_BOOKINGS).child(bookingId)

    private fun userBookingsRef(uid: String) =
        firebaseDatabase.getReference(Config.FIREBASE_USER_BOOKINGS).child(uid)

    // --- CREATE: Firebase trước, Room sau ---
    override suspend fun createBooking(booking: Booking): Result<String> =
        withContext(dispatcher) {
            runCatching {
                val uid = requireUid()
                println("DEBUG uid = $uid")

                val ref = firebaseDatabase.getReference(Config.FIREBASE_BOOKINGS).push()
                val bookingId = ref.key ?: error("push key null")

                val bookingWithId = booking.copy(id = bookingId)

                val updates = hashMapOf<String, Any>(
                    "${Config.FIREBASE_BOOKINGS}/$bookingId" to bookingWithId,
                    "${Config.FIREBASE_USER_BOOKINGS}/$uid/$bookingId" to true
                )

                println("DEBUG updates = $updates")

                firebaseDatabase.reference.updateChildren(updates).await()

                bookingDao.insertBooking(bookingWithId.toEntity(uid))
                bookingId
            }.onFailure {
                it.printStackTrace()
            }
        }
    // --- GET: Room trước, Firebase fallback ---
    override suspend fun getBookings(): Result<List<Booking>> =
        withContext(dispatcher) {
            runCatching {
                val uid = requireUid()
                val cached = bookingDao.getBookings(uid)
                if (cached.isNotEmpty()) {
                    refreshFromFirebase(uid)
                    return@runCatching cached.map { it.toDomain() }
                }
                fetchFromFirebase(uid).also { fresh ->
                    bookingDao.clearBookings(uid)
                    bookingDao.insertBookings(fresh.map { it.toEntity(uid) })
                }
            }
        }

    // --- GET BY ID: Room trước, Firebase fallback ---
    override suspend fun getBookingById(bookingId: String): Result<Booking> =
        withContext(dispatcher) {
            runCatching {
                bookingDao.getBookingById(bookingId)?.toDomain()
                    ?: bookingRef(bookingId).get().await().toBooking()
                    ?: throw NoSuchElementException("Booking not found")
            }
        }

    // --- CANCEL: Firebase trước, Room sync sau ---
    override suspend fun cancelBooking(bookingId: String): Result<Unit> =
        withContext(dispatcher) {
            runCatching {
                bookingRef(bookingId).child("status")
                    .setValue(BookingStatus.CANCELLED.name).await()

                bookingDao.updateStatus(bookingId, BookingStatus.CANCELLED.name)
                Unit
            }
        }

    // --- UPDATE STATUS ---
    override suspend fun updateBookingStatus(
        bookingId: String,
        status: BookingStatus
    ): Result<Unit> = withContext(dispatcher) {
        runCatching {
            bookingRef(bookingId).child("status").setValue(status.name).await()
            bookingDao.updateStatus(bookingId, status.name)
            Unit
        }
    }

    // --- OBSERVE: Room Flow (reactive, không tốn Firebase reads) ---
    override fun observeBookings(): Flow<List<Booking>> {
        val uid = runCatching { requireUid() }.getOrDefault("")
        return bookingDao.observeBookings(uid).map { list -> list.map { it.toDomain() } }
    }

    // --- Private helpers ---
    private suspend fun fetchFromFirebase(uid: String): List<Booking> =
        coroutineScope {
            val indexSnapshot = userBookingsRef(uid).get().await()
            val bookingIds = indexSnapshot.children.mapNotNull { it.key }

            if (bookingIds.isEmpty()) return@coroutineScope emptyList()

            // ✅ Tất cả bookings fetch cùng lúc
            bookingIds
                .map { id -> async { bookingRef(id).get().await() } }
                .mapNotNull { deferred -> deferred.await().toBooking() }
                .sortedByDescending { it.createdAt }
        }

    private suspend fun refreshFromFirebase(uid: String) {
        try {
            val fresh = fetchFromFirebase(uid)
            bookingDao.clearBookings(uid)
            bookingDao.insertBookings(fresh.map { it.toEntity(uid) })
        } catch (_: Exception) { }
    }

    private fun DataSnapshot.toBooking(): Booking? = runCatching {
        val id = key ?: return null
        Booking(
            id = id,
            clinicId = child("clinicId").getValue(String::class.java) ?: return null,
            clinicName = child("clinicName").getValue(String::class.java) ?: "",
            clinicAddress = child("clinicAddress").getValue(String::class.java) ?: "",
            clinicDistrict = child("clinicDistrict").getValue(String::class.java) ?: "",
            clinicCity = child("clinicCity").getValue(String::class.java) ?: "",
            insuranceSupported = child("insuranceSupported").getValue(Boolean::class.java) ?: false,
            departmentId = child("departmentId").getValue(String::class.java) ?: "",
            doctorId = child("doctorId").getValue(String::class.java) ?: "",
            doctorName = child("doctorName").getValue(String::class.java) ?: "",
            patientId = child("patientId").getValue(String::class.java) ?: "",
            patientName = child("patientName").getValue(String::class.java) ?: "",
            patientPhone = child("patientPhone").getValue(String::class.java) ?: "",
            patientDateOfBirth = child("patientDateOfBirth").getValue(String::class.java) ?: "",
            patientHealthInsurance = child("patientHealthInsurance").getValue(String::class.java) ?: "",
            specialty = child("specialty").getValue(String::class.java) ?: "",
            service = child("service").getValue(String::class.java) ?: "",
            bookingType = child("bookingType").getValue(String::class.java) ?: "",
            slotId = child("slotId").getValue(String::class.java) ?: "",
            appointmentDate = child("appointmentDate").getValue(String::class.java) ?: "",
            appointmentTime = child("appointmentTime").getValue(String::class.java) ?: "",
            consultationFee = child("consultationFee").getValue(Long::class.java) ?: 0L,
            paymentMethod = child("paymentMethod").getValue(String::class.java) ?: "",
            status = runCatching {
                BookingStatus.valueOf(child("status").getValue(String::class.java) ?: "")
            }.getOrDefault(BookingStatus.PENDING),
            createdAt = child("createdAt").getValue(Long::class.java) ?: System.currentTimeMillis()
        )
    }.getOrNull()
}