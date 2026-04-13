package com.example.appointmentschedulingapp.data.local.dao

import androidx.room.*
import com.example.appointmentschedulingapp.data.local.entity.PatientProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientProfileDao {

    @Query("SELECT * FROM patient_profiles WHERE userId = :userId")
    fun observeProfiles(userId: String): Flow<List<PatientProfileEntity>>

    @Query("SELECT * FROM patient_profiles WHERE userId = :userId")
    suspend fun getProfiles(userId: String): List<PatientProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<PatientProfileEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: PatientProfileEntity)

    @Query("SELECT * FROM patient_profiles WHERE id = :profileId")
    suspend fun getProfileById(profileId: String): PatientProfileEntity?

    @Update
    suspend fun updateProfile(profile: PatientProfileEntity)

    @Query("DELETE FROM patient_profiles WHERE id = :profileId")
    suspend fun deleteProfile(profileId: String)

    @Query("DELETE FROM patient_profiles WHERE userId = :userId")
    suspend fun clearProfiles(userId: String)
}