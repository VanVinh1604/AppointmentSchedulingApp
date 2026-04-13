package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.PatientProfile
import kotlinx.coroutines.flow.Flow

interface PatientRepository {

    suspend fun createPatientProfile(profile: PatientProfile): Result<Unit>


    suspend fun getPatientProfiles(): Result<List<PatientProfile>>

    suspend fun getPatientProfileById(profileId: String): Result<PatientProfile>  // ← thêm
    suspend fun updatePatientProfile(profile: PatientProfile): Result<Unit>        // ← thêm
    suspend fun deletePatientProfile(profileId: String): Result<Unit>              // ← thêm
    fun observePatientProfiles(): Flow<List<PatientProfile>>
}