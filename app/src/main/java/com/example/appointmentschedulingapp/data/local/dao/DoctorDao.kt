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

    @Query("SELECT * FROM doctors WHERE departmentId = :departmentId")
    suspend fun getDoctorsByDepartment(departmentId: String): List<DoctorEntity>

    @Query("SELECT * FROM doctors")
    suspend fun getAllDoctors(): List<DoctorEntity>

    @Query("SELECT * FROM doctors WHERE id = :doctorId")
    suspend fun getDoctorById(doctorId: String): DoctorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctors(doctors: List<DoctorEntity>)

    @Query("DELETE FROM doctors WHERE clinicId = :clinicId")
    suspend fun clearDoctorsByClinic(clinicId: String)
}