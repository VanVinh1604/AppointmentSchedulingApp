package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.domain.repository.AuthRepository
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(phone: String): Result<String> {
        return repository.sendOtp(phone)
    }
}