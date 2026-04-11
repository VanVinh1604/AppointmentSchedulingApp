package com.example.appointmentschedulingapp.domain.usecase.athuUsecase

import android.app.Activity
import com.example.appointmentschedulingapp.domain.repository.AuthRepository
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        activity: Activity,
        phone: String
    ): Result<String> {
        return repository.sendOtp(activity, phone)
    }
}