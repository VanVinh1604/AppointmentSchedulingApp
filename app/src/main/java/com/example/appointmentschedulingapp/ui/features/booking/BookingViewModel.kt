package com.example.appointmentschedulingapp.ui.features.booking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.appointmentschedulingapp.domain.usecase.CreateBookingUseCase
import com.example.appointmentschedulingapp.domain.usecase.GetClinicByIdUseCase
import com.example.appointmentschedulingapp.domain.usecase.GetDoctorsByClinicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getClinicByIdUseCase: GetClinicByIdUseCase,
    private val createBookingUseCase: CreateBookingUseCase,
    private val getDoctorsByClinicUseCase: GetDoctorsByClinicUseCase
) : ViewModel() {

    private companion object {
        const val TAG = "BookingViewModel"
    }

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: BookingEvent) {
        when (event) {
            is BookingEvent.LoadClinic -> loadClinic(event.id)
            is BookingEvent.SelectClinic -> {
                _uiState.update { it.copy(selectedClinic = event.clinic) }
            }

            is BookingEvent.SelectService -> {
                _uiState.update {
                    it.copy(selectedBookingType = event.service)
                }
            }

            is BookingEvent.UpdateSpecialty -> {
                _uiState.update {
                    it.copy(
                        selectedSpecialty = event.specialty,
                        selectedDoctor = null,
                        doctors = emptyList()
                    )
                }

//                loadDoctorsBySpecialty()
            }

            is BookingEvent.SetStep -> {
                _uiState.update { it.copy(currentStep = event.step) }
            }

            is BookingEvent.SelectDate -> {
                _uiState.update { it.copy(selectedDate = event.date) }
            }

            is BookingEvent.SelectTime -> {
                _uiState.update { it.copy(selectedTime = event.time) }
            }

            is BookingEvent.SelectPatient -> {
                _uiState.update {
                    it.copy(
                        selectedPatientId = event.id,
                        patientName = event.name
                    )
                }
            }

            BookingEvent.ConfirmBooking -> confirmBooking()
        }
    }

    private fun loadClinic(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val clinic = getClinicByIdUseCase(id)

            _uiState.update {
                it.copy(
                    selectedClinic = clinic,
                    isLoading = false,
                    errorMessage = if (clinic == null) "Không tìm thấy cơ sở y tế" else null
                )
            }
        }
    }

//    private fun loadDoctorsBySpecialty() {
//        viewModelScope.launch {
//            val clinicId = _uiState.value.selectedClinic?.id ?: return@launch
//            val specialty = _uiState.value.selectedSpecialty
//
//            Log.d(TAG, "clinicId=$clinicId")
//            Log.d(TAG, "selectedSpecialty=$specialty")
//
//            val result = getDoctorsByClinic(clinicId)
//
//            result.onSuccess { allDoctors ->
//                Log.d(TAG, "SUCCESS: total doctors = ${allDoctors.size}")
//
//                allDoctors.forEach {
//                }
//
//                val filtered = allDoctors.filter {
//                }
//
//                Log.d(TAG, "filtered doctors size = ${filtered.size}")
//
//                _uiState.update {
//                    it.copy(doctors = filtered)
//                }
//
//            }.onFailure { e ->
//                Log.e(TAG, "FAILED loadDoctorsBySpecialty: ${e.message}", e)
//            }
//        }
//    }

    private fun confirmBooking() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = createBookingUseCase(_uiState.value)

            result.onSuccess { bookingId ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        bookingId = bookingId
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            }
        }
    }
}