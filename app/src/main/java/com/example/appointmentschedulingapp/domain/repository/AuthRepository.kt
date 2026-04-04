package com.example.appointmentschedulingapp.domain.repository

interface AuthRepository {
    suspend fun sendOtp(phone: String): Result<String>
    suspend fun verifyOtp(verificationId: String, otp: String): Result<Boolean>
}