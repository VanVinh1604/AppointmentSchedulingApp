package com.example.appointmentschedulingapp.ui.features.doctor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.core.helper.ErrorHelper
import com.example.appointmentschedulingapp.domain.usecase.doctorUscase.GetDoctorByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorDetailViewModel @Inject constructor(
    private val getDoctorByIdUseCase: GetDoctorByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorDetailUiState())
    val uiState: StateFlow<DoctorDetailUiState> = _uiState

    fun loadDoctor(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            getDoctorByIdUseCase(id)
                .onSuccess { doctor ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        doctor = doctor
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = ErrorHelper.toFriendlyMessage(e)
                    )
                }
        }
    }
}