package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.Department

interface DepartmentRepository {
    suspend fun getDepartmentsByClinic(
        clinicId: String
    ): Result<List<Department>>
}