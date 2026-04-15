package com.example.appointmentschedulingapp.ui.features.clinicDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.usecase.clinicUsecase.GetClinicByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicDetailViewModel @Inject constructor(
    private val getClinicByIdUseCase: GetClinicByIdUseCase
) : ViewModel() {

    private val _clinic = MutableStateFlow<Clinic?>(null)
    val clinic = _clinic.asStateFlow()

    fun loadClinic(id: String) {
        viewModelScope.launch {
            _clinic.value = getClinicByIdUseCase(id)
        }
    }
}