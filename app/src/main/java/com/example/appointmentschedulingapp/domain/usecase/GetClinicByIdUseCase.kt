package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.repository.ClinicRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetClinicByIdUseCase @Inject constructor(
    private val repository: ClinicRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(id: String): Clinic? =
        withContext(dispatcher){
            repository.getClinicById(id)
    }
}