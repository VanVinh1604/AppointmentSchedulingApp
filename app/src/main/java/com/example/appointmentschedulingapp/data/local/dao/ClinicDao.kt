package com.example.appointmentschedulingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appointmentschedulingapp.data.local.entity.ClinicEntity

@Dao
interface ClinicDao {

    @Query("SELECT * FROM clinics")
    suspend fun getClinics(): List<ClinicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClinics(clinics: List<ClinicEntity>)
}