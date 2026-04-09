package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.Doctor

interface DoctorRepository {

    suspend fun getAllDoctors(): Result<List<Doctor>>

    suspend fun getDoctorsByClinic(clinicId: String): Result<List<Doctor>>

    suspend fun getDoctorById(doctorId: String): Result<Doctor?>
}