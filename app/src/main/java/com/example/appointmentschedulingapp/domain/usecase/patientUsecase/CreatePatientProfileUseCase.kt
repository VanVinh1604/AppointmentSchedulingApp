package com.example.appointmentschedulingapp.domain.usecase.patientUsecase

import com.example.appointmentschedulingapp.domain.model.PatientProfile
import com.example.appointmentschedulingapp.domain.repository.PatientRepository
import javax.inject.Inject

class CreatePatientProfileUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(profile: PatientProfile): Result<Unit> {
        return repository.createPatientProfile(profile)
    }
}