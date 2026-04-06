package com.example.appointmentschedulingapp.ui.features.auth

data class AuthUiState(
    val isLoading: Boolean = false,

    val phone: String = "",
    val verificationId: String? = null,
    val error: String? = null,
    val isOtpSent: Boolean = false,
    val isVerified: Boolean = false,

    val resendSeconds: Int = 60

)