package com.example.appointmentschedulingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appointmentschedulingapp.data.local.entity.DoctorEntity

@Dao
interface DoctorDao {

    @Query("SELECT * FROM doctors WHERE clinicId = :clinicId")
    suspend fun getDoctorsByClinic(clinicId: String): List<DoctorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<DoctorEntity>)
}