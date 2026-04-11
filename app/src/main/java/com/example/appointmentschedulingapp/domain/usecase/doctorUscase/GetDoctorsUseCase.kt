package com.example.appointmentschedulingapp.domain.usecase.doctorUscase

import com.example.appointmentschedulingapp.domain.model.Doctor
import com.example.appointmentschedulingapp.domain.repository.DoctorRepository
import javax.inject.Inject

class GetDoctorsUseCase @Inject constructor(
    private val repository: DoctorRepository
) {
    suspend fun execute(): Result<List<Doctor>> {
        return repository.getAllDoctors()
    }
}