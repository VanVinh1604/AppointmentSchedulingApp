package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.repository.ClinicRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetClinicsUseCase @Inject constructor(
    private val repository: ClinicRepository,
    ) {
    suspend operator fun invoke(): List<Clinic>  {
          return  repository.getClinics()

        }
}