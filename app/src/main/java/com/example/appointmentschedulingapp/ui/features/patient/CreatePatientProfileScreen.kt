package com.example.appointmentschedulingapp.ui.features.patient


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appointmentschedulingapp.ui.features.patient.components.BottomSaveBar
import com.example.appointmentschedulingapp.ui.features.patient.components.PatientTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePatientProfileScreen(
    onBack: (Boolean) -> Unit
) {
    val viewModel: CreatePatientProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBack(true)
        }
    }

    val isFormValid =
        uiState.fullName.isNotBlank() &&
                uiState.dateOfBirth.isNotBlank() &&
                uiState.gender.isNotBlank() &&
                uiState.phoneNumber.isNotBlank()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            PatientTopBar(onBack = {onBack(false) })
        },
        bottomBar = {
            BottomSaveBar(
                isDefault = uiState.isDefault,
                isLoading = uiState.isLoading,
                isFormValid = isFormValid,
                onToggle = {
                    viewModel.onEvent(CreatePatientProfileEvent.DefaultChanged(it))
                },
                onSave = { viewModel.onEvent(CreatePatientProfileEvent.SaveProfile) }

            )
        }
    ) { padding ->
        CreatePatientProfileContent(
            padding = padding,
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}