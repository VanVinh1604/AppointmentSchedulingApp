package com.example.appointmentschedulingapp.data.local.dao

import androidx.room.*
import com.example.appointmentschedulingapp.data.local.entity.BookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeBookings(userId: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getBookings(userId: String): List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE id = :bookingId")
    suspend fun getBookingById(bookingId: String): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<BookingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    suspend fun updateStatus(bookingId: String, status: String)

    @Query("DELETE FROM bookings WHERE userId = :userId")
    suspend fun clearBookings(userId: String)
}