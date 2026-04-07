package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.domain.model.Doctor
import com.example.appointmentschedulingapp.domain.repository.DoctorRepository
import javax.inject.Inject

class GetDoctorsUseCase @Inject constructor(
    private val repository: DoctorRepository
) {
    // Luồng lấy bác sĩ theo khoa
    suspend fun execute(departmentId: String? = null): Result<List<Doctor>> {
        return if (departmentId == null) {
            repository.getAllDoctors()
        } else {
            repository.getDoctorsByDepartment(departmentId)
        }
    }
}