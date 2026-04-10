package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.PatientProfile

interface PatientRepository {

    suspend fun createPatientProfile(profile: PatientProfile): Result<Unit>


    suspend fun getPatientProfiles(): Result<List<PatientProfile>>
}