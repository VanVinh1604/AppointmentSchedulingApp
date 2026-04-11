package com.example.appointmentschedulingapp.domain.usecase.athuUsecase

import com.example.appointmentschedulingapp.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        verificationId: String,
        otp: String
    ): Result<Boolean> {
        return repository.verifyOtp(verificationId, otp)
    }
}