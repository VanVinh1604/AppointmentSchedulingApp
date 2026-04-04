package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.Clinic

interface ClinicRepository {
    suspend fun getClinics(): List<Clinic>
    suspend fun getClinicById(id: String): Clinic?
}