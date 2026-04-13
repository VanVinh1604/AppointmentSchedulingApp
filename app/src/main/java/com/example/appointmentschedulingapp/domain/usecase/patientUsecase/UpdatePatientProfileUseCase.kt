package com.example.appointmentschedulingapp.domain.usecase.patientUsecase

import com.example.appointmentschedulingapp.domain.model.PatientProfile
import com.example.appointmentschedulingapp.domain.repository.PatientRepository
import javax.inject.Inject

class UpdatePatientProfileUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(profile: PatientProfile) =
        repository.updatePatientProfile(profile)
}