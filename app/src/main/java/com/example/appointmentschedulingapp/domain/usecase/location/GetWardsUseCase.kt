package com.example.appointmentschedulingapp.domain.usecase.location

import com.example.appointmentschedulingapp.domain.repository.LocationRepository

class GetWardsUseCase(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(code: Int) =
        repository.getWardsByProvince(code)
}