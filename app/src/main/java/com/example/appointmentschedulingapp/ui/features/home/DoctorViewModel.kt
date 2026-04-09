package com.example.appointmentschedulingapp.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.Doctor
import com.example.appointmentschedulingapp.domain.usecase.GetDoctorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Collections.list
import javax.inject.Inject

@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val getDoctorsUseCase: GetDoctorsUseCase
) : ViewModel() {

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors = _doctors.asStateFlow()

    fun loadDoctors(departmentId: String? = null) {
        viewModelScope.launch {
            getDoctorsUseCase.execute().onSuccess { list ->
                println("DEBUG_DOCTOR: Đã lấy được ${list.size} bác sĩ") // Thêm dòng này
                _doctors.value = list
            }.onFailure {
                println("DEBUG_DOCTOR: Lỗi rồi: ${it.message}") // Thêm dòng này
            }
        }
    }
}