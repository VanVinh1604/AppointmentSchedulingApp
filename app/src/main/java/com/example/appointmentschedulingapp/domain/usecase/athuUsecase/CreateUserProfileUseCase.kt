package com.example.appointmentschedulingapp.domain.usecase.athuUsecase

import com.example.appointmentschedulingapp.domain.repository.AuthRepository
import javax.inject.Inject

class CreateUserProfileUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String): Result<Unit> {
        return repository.createUserProfile(phone)
    }
}