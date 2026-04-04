package com.example.appointmentschedulingapp.ui.features.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.appointmentschedulingapp.domain.usecase.CreateBookingUseCase
import com.example.appointmentschedulingapp.domain.usecase.GetClinicByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getClinicByIdUseCase: GetClinicByIdUseCase,
    private val createBookingUseCase: CreateBookingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: BookingEvent) {
        when (event) {
            is BookingEvent.LoadClinic -> loadClinic(event.id)
            is BookingEvent.SelectClinic -> {
                _uiState.update { it.copy(selectedClinic = event.clinic) }
            }

            is BookingEvent.SelectService -> {
                _uiState.update { it.copy(selectedService = event.service) }
            }

            is BookingEvent.UpdateSpecialty -> {
                _uiState.update { it.copy(selectedSpecialty = event.specialty) }
            }

            is BookingEvent.SetStep -> {
                _uiState.update { it.copy(currentStep = event.step) }
            }

            is BookingEvent.SelectRoom -> {
                _uiState.update { it.copy(selectedRoom = event.room) }
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