package com.example.appointmentschedulingapp.domain.usecase.patientUsecase

import com.example.appointmentschedulingapp.domain.repository.PatientRepository
import javax.inject.Inject

class DeletePatientProfileUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(profileId: String) =
        repository.deletePatientProfile(profileId)
}