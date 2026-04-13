package com.example.appointmentschedulingapp.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.usecase.patientUsecase.GetPatientProfilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getPatientProfilesUseCase: GetPatientProfilesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfiles()
    }

    fun loadProfiles() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        getPatientProfilesUseCase()
            .onSuccess { profiles ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    profiles = profiles
                )
            }
            .onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Không thể tải hồ sơ"
                )
            }
    }
}