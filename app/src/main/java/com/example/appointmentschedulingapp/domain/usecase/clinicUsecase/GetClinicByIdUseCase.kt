package com.example.appointmentschedulingapp.domain.usecase.clinicUsecase

import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.repository.ClinicRepository

import javax.inject.Inject

class GetClinicByIdUseCase @Inject constructor(
    private val repository: ClinicRepository,
) {
    suspend operator fun invoke(id: String): Clinic? {
            return repository.getClinicById(id)

    }
}