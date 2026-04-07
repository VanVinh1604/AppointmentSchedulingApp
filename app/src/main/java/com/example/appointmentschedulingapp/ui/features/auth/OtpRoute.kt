package com.example.appointmentschedulingapp.ui.features.auth

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OtpRoute(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current
    val state by viewModel.uiState.collectAsState()

    OtpVerificationScreen(
        phoneNumber = state.phone,
        onBack = onBack,

        resendSeconds = state.resendSeconds,
        isLoading = state.isLoading,         // ← thêm
        errorMessage = state.error,           // ← thêm
        onVerify = { otp ->
            viewModel.verifyOtp(otp)
        },
        onResendOtp = {
            activity?.let { viewModel.sendOtp(it, state.phone) }
        },
        showOverlay = state.showAuthOverlay,
        authSuccess = state.authSuccess,
        authMessage = state.authMessage

    )

    LaunchedEffect(Unit) {
        viewModel.clearError()
        viewModel.event.collect { event ->
            when (event) {
                AuthEvent.NavigateToHome -> onSuccess()
                else -> Unit
            }
        }
    }
}