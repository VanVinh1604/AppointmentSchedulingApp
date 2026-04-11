package com.example.appointmentschedulingapp.domain.usecase.doctorUscase

import com.example.appointmentschedulingapp.domain.model.Doctor
import com.example.appointmentschedulingapp.domain.repository.DoctorRepository
import javax.inject.Inject

class GetDoctorByIdUseCase @Inject constructor(
    private val repository: DoctorRepository
) {
    suspend operator fun invoke(id: String): Result<Doctor> {
        return repository.getDoctorById(id).fold(
            onSuccess = { doctor ->
                doctor?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Không tìm thấy bác sĩ")
                )
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}