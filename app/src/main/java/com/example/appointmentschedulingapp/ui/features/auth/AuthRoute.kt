package com.example.appointmentschedulingapp.ui.features.auth

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AuthRoute(
    onBack: () -> Unit,
    onNavigateOtp: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current  // ← sửa ở đây
    val state by viewModel.uiState.collectAsState()

    AuthScreen(
        onBack = onBack,
        isLoading = state.isLoading,        // ← truyền xuống
        errorMessage = state.error,          // ← truyền xuống
        onContinue = { phone ->
            activity?.let { viewModel.sendOtp(it, phone) }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.clearError()
        viewModel.event.collect { event ->
            when (event) {
                is AuthEvent.NavigateToOtp -> onNavigateOtp(event.phone)
                else -> Unit
            }
        }
    }
}