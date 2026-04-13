package com.example.appointmentschedulingapp.domain.usecase.location

import com.example.appointmentschedulingapp.domain.repository.LocationRepository

class GetProvincesUseCase(
    private val repository: LocationRepository
) {
    suspend operator fun invoke() = repository.getProvinces()
}