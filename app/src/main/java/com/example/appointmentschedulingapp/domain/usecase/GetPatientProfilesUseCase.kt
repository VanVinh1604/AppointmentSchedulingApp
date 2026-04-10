package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.domain.model.PatientProfile
import com.example.appointmentschedulingapp.domain.repository.PatientRepository
import javax.inject.Inject

class GetPatientProfilesUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(): Result<List<PatientProfile>> {
        return repository.getPatientProfiles()
    }
}