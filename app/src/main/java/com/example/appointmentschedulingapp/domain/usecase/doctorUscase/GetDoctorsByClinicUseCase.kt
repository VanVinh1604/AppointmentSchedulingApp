package com.example.appointmentschedulingapp.domain.usecase.doctorUscase

import com.example.appointmentschedulingapp.domain.model.Doctor
import com.example.appointmentschedulingapp.domain.repository.DoctorRepository
import javax.inject.Inject

class GetDoctorsByClinicUseCase @Inject constructor(
    private val repository: DoctorRepository
) {
    suspend operator fun invoke(
        clinicId: String
    ): Result<List<Doctor>> {
        return repository.getDoctorsByClinic(clinicId)
    }
}

