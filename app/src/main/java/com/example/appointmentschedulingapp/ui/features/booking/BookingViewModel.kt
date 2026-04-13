package com.example.appointmentschedulingapp.ui.features.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.payment.momoPayment.MomoCallbackBus
import com.example.appointmentschedulingapp.domain.payment.PaymentResult
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.ConfirmBookingWithPaymentUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.GetBookingByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.appointmentschedulingapp.domain.usecase.clinicUsecase.GetClinicByIdUseCase
import com.example.appointmentschedulingapp.domain.usecase.doctorUscase.GetDoctorsByClinicUseCase
import com.example.appointmentschedulingapp.domain.usecase.patientUsecase.GetPatientProfilesUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.GetBookingsUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.ObserveBookingsUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.UpdateBookingStatusUseCase

import com.example.appointmentschedulingapp.ui.features.booking.steps.components.DEFAULT_PAYMENT_METHODS
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getClinicByIdUseCase: GetClinicByIdUseCase,
    private val confirmBookingWithPaymentUseCase: ConfirmBookingWithPaymentUseCase,
    private val getDoctorsByClinicUseCase: GetDoctorsByClinicUseCase,
    private val getPatientProfilesUseCase: GetPatientProfilesUseCase,
    private val getBookingsUseCase: GetBookingsUseCase,
    private val getBookingByIdUseCase: GetBookingByIdUseCase,
    private val observeBookingsUseCase: ObserveBookingsUseCase,      // ← thêm
    private val updateBookingStatusUseCase: UpdateBookingStatusUseCase

) : ViewModel() {

    private companion object {
        const val TAG = "BookingViewModel"
        const val POLLING_INTERVAL = 2000L // 2 giây
        const val MAX_POLLING_ATTEMPTS = 15 // 30 giây tối đa
    }


    init {
        // ✅ Nếu app bị kill và restore, tự động observe booking đang pending
        val pendingBookingId = savedStateHandle.get<String>("bookingId")
        val pendingMomoUrl = savedStateHandle.get<String>("momoPayUrl")
        if (!pendingBookingId.isNullOrEmpty() && !pendingMomoUrl.isNullOrEmpty()) {
            observeBookingStatus(pendingBookingId)
        }

        viewModelScope.launch {
            MomoCallbackBus.events.collect { orderId ->
                if (_uiState.value.momoPayUrl != null) {
                    val bookingId = if (orderId == "__momo_success__") {
                        _uiState.value.bookingId
                    } else {
                        orderId
                    }
                    if (bookingId.isNotEmpty()) {
                        onMomoPaymentReturned(bookingId)
                    }
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(
        BookingUiState(
            // ✅ Khôi phục bookingId và momoPayUrl từ SavedStateHandle
            bookingId = savedStateHandle.get<String>("bookingId") ?: "",
            momoPayUrl = savedStateHandle.get<String>("momoPayUrl")
        )
    )

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

            is BookingEvent.ShowStep1Error -> {
                _uiState.update {
                    it.copy(step1Error = event.message)
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

            is BookingEvent.SelectPaymentMethod -> {
                _uiState.update {
                    it.copy(selectedPaymentMethod = event.method)
                }
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

            is BookingEvent.PaymentSuccess -> {
                _uiState.update {
                    it.copy(
                        isSuccess = true,
                        isLoading = false
                    )
                }
            }

            is BookingEvent.PaymentFailure -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = event.message
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

    fun loadPatientProfiles() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            getPatientProfilesUseCase()
                .onSuccess { profiles ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            patientProfiles = profiles,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Không tải được hồ sơ bệnh nhân"
                        )
                    }
                }
        }
    }


    private fun confirmBooking() {
        viewModelScope.launch {
            val method = _uiState.value.selectedPaymentMethod
            val selectedMethod = DEFAULT_PAYMENT_METHODS.find { it.id == method }

            if (selectedMethod?.isImplemented != true) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Phương thức này đang được phát triển",
                        isLoading = false
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            confirmBookingWithPaymentUseCase(_uiState.value)
                .onSuccess(::handlePaymentResult)
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    private fun handlePaymentResult(paymentResult: PaymentResult) {
        when (paymentResult) {
            is PaymentResult.Redirect -> {
                // ✅ Lưu vào SavedStateHandle để survive process death
                savedStateHandle["bookingId"] = paymentResult.bookingId
                savedStateHandle["momoPayUrl"] = paymentResult.url

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        bookingId = paymentResult.bookingId,
                        momoPayUrl = paymentResult.url
                    )
                }
                observeBookingStatus(paymentResult.bookingId)
            }

            is PaymentResult.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        bookingId = paymentResult.bookingId
                    )
                }
            }

            is PaymentResult.Failure -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = paymentResult.message
                    )
                }
            }
        }
    }

//    fun isStep1Valid(): Boolean {
//        val state = _uiState.value
//        return state.selectedSpecialty.isNotBlank() &&
//                state.selectedBookingType.isNotBlank() &&
//                state.selectedDate.isNotBlank() &&
//                state.selectedTime.isNotBlank()
//    }
//    fun validateStep1(): Boolean {
//        val isValid = isStep1Valid()
//
//        _uiState.update {
//            it.copy(
//                step1Error = if (!isValid)
//                    "Vui lòng chọn đầy đủ thông tin khám"
//                else null
//            )
//        }
//
//        return isValid
//    }
    // BookingViewModel.kt
private fun observeBookingStatus(bookingId: String) {
    viewModelScope.launch {
        getBookingsUseCase() // seed Room trước

        observeBookingsUseCase()
            .collect { bookings ->
                val booking = bookings.firstOrNull { it.id == bookingId } ?: return@collect
                if (booking.status.name in listOf("PAID", "CONFIRMED", "COMPLETED")) {
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = true, bookingId = bookingId)
                    }
                }
            }
    }
}

    fun onMomoPaymentReturned(bookingId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            updateBookingStatusUseCase(bookingId, BookingStatus.CONFIRMED)
                .onSuccess {
                    savedStateHandle.remove<String>("bookingId")
                    savedStateHandle.remove<String>("momoPayUrl")
                    _uiState.update {
                        it.copy(isLoading = false, isSuccess = true, bookingId = bookingId)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Lỗi xác nhận thanh toán: ${error.message}")
                    }
                }
        }
    }
}