package com.example.appointmentschedulingapp.domain.usecase.athuUsecase

import com.example.appointmentschedulingapp.domain.repository.AuthRepository
import javax.inject.Inject

class CheckProfileUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.isNewUser()
    }
}