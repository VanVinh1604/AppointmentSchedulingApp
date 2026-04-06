package com.example.appointmentschedulingapp.domain.repository

import android.app.Activity

interface AuthRepository {
    suspend fun sendOtp(
        activity: Activity,
        phone: String
    ): Result<String>

    suspend fun verifyOtp(
        verificationId: String,
        otp: String
    ): Result<Boolean>
}