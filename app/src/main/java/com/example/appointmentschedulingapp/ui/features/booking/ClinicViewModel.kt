package com.example.appointmentschedulingapp.ui.features.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.usecase.GetClinicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicViewModel @Inject constructor(
    private val getClinicsUseCase: GetClinicsUseCase,
) : ViewModel() {

    private val _clinics = MutableStateFlow<List<Clinic>>(emptyList())
    val clinics = _clinics.asStateFlow()

    init {
        loadClinics()
    }

    private fun loadClinics() {
        viewModelScope.launch {
            _clinics.value = getClinicsUseCase()
        }
    }
}