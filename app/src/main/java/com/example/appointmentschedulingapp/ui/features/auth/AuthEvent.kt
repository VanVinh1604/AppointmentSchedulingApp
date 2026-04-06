package com.example.appointmentschedulingapp.ui.features.auth

sealed class AuthEvent {
    data class NavigateToOtp(val phone: String) : AuthEvent()
    object NavigateToHome : AuthEvent()
    data class ShowError(val message: String) : AuthEvent()
}