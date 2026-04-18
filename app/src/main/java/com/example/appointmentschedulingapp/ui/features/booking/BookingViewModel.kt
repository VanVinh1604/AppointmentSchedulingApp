package com.example.appointmentschedulingapp.ui.features.booking

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.appointmentschedulingapp.domain.model.ConfirmBookingRequest
import com.example.appointmentschedulingapp.domain.payment.momoPayment.MomoCallbackBus
import com.example.appointmentschedulingapp.domain.payment.PaymentResult
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.ConfirmBookingWithPaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.appointmentschedulingapp.domain.usecase.clinicUsecase.GetClinicByIdUseCase
import com.example.appointmentschedulingapp.domain.usecase.patientUsecase.GetPatientProfilesUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.GetBookingsUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.ObserveBookingsUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.UpdateBookingStatusUseCase
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus
import com.example.appointmentschedulingapp.workers.PaymentRetryWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getClinicByIdUseCase: GetClinicByIdUseCase,
    private val confirmBookingWithPaymentUseCase: ConfirmBookingWithPaymentUseCase,
//    private val getDoctorsByClinicUseCase: GetDoctorsByClinicUseCase,
    private val getPatientProfilesUseCase: GetPatientProfilesUseCase,
    private val getBookingsUseCase: GetBookingsUseCase,
    private val observeBookingsUseCase: ObserveBookingsUseCase,      // ← thêm
    @ApplicationContext private val context: Context,
    private val updateBookingStatusUseCase: UpdateBookingStatusUseCase

) : ViewModel() {

    private var observeJob: kotlinx.coroutines.Job? = null

    init {
        //  Nếu app bị kill và restore, tự động observe booking đang pending
        val pendingBookingId = savedStateHandle.get<String>("bookingId")
        val pendingMomoUrl = savedStateHandle.get<String>("momoPayUrl")
        if (!pendingBookingId.isNullOrEmpty() && !pendingMomoUrl.isNullOrEmpty()) {
            observeBookingStatus(pendingBookingId)
        }

        viewModelScope.launch {
            MomoCallbackBus.events.collect { (orderId, resultCode) ->
                onMomoPaymentReturned(orderId, resultCode)
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
                    // ✅ Reset các field liên quan khi load clinic mới
                    selectedSpecialty = "",
                    selectedBookingType = "",
                    selectedDate = "",
                    selectedTime = "",
                    isCancelled = false,
                    isSuccess = false,
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
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isCancelled = false,
                    errorMessage = null
                )
            }
            val state = _uiState.value

            // ✅ Nếu đã có bookingId từ lần trước (re-try cùng booking)
            // thì KHÔNG tạo mới, chỉ tạo lại payment URL
            val existingBookingId = state.bookingId.ifEmpty { null }

            val request = ConfirmBookingRequest(
                clinicId = state.selectedClinic?.id ?: "",
                clinicName = state.selectedClinic?.name ?: "",
                clinicAddress = state.selectedClinic?.address ?: "",
                consultationFee = state.selectedClinic?.consultationFee ?: 0L,
                patientId = state.selectedPatientId,
                patientName = state.patientName,
                specialty = state.selectedSpecialty,
                service = state.selectedBookingType,
                appointmentDate = state.selectedDate,
                appointmentTime = state.selectedTime,
                paymentMethod = state.selectedPaymentMethod,
                existingBookingId = existingBookingId
            )

            confirmBookingWithPaymentUseCase(request)
                .onSuccess(::handlePaymentResult)
                .onFailure {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = it.errorMessage)
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
                schedulePaymentRetry(paymentResult.bookingId)
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

    private fun observeBookingStatus(bookingId: String) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            observeBookingsUseCase()
                .collect { bookings ->
                    val booking = bookings.firstOrNull { it.id == bookingId } ?: return@collect

                    val currentState = _uiState.value

                    // ✅ Bỏ check errorMessage, chỉ giữ isSuccess
                    // vì errorMessage != null sau cancel sẽ chặn cả lần thanh toán tiếp theo
                    if (currentState.isSuccess) return@collect

                    when (booking.status) {
                        BookingStatus.PAID,
                        BookingStatus.CONFIRMED,
                        BookingStatus.COMPLETED -> {
                            // ✅ Thêm check isCancelled để tránh overwrite
                            if (currentState.isCancelled) return@collect

                            savedStateHandle.remove<String>("bookingId")
                            savedStateHandle.remove<String>("momoPayUrl")
                            _uiState.update {
                                it.copy(isLoading = false, isSuccess = true)
                            }
                            observeJob?.cancel()
                        }

                        BookingStatus.CANCELLED,
                        BookingStatus.FAILED -> {
                            savedStateHandle.remove<String>("bookingId")
                            savedStateHandle.remove<String>("momoPayUrl")
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = false,
                                    errorMessage = "Thanh toán thất bại"
                                )
                            }
                            observeJob?.cancel()
                        }

                        else -> Unit
                    }
                }
        }

        // Fallback timeout 15s
        viewModelScope.launch {
            kotlinx.coroutines.delay(15_000)
            // ✅ Thêm check isCancelled
            if (_uiState.value.isLoading && !_uiState.value.isCancelled) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Xác nhận quá lâu, vui lòng thử lại")
                }
            }
        }
    }

    fun onMomoPaymentReturned(bookingId: String, resultCode: Int) {
        viewModelScope.launch {
            if (resultCode != 0) {
                // ← Cancel observeJob TRƯỚC KHI update backend
                observeJob?.cancel()
                cancelPaymentRetryWorker(bookingId)
                _uiState.update { it.copy(isLoading = true) }

                updateBookingStatusUseCase(bookingId, BookingStatus.FAILED)
                    .onSuccess {
                        savedStateHandle.remove<String>("bookingId")
                        savedStateHandle.remove<String>("momoPayUrl")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = false,
                                isCancelled = true,  // ← đánh dấu
                                momoPayUrl = null,
                                errorMessage = "Thanh toán MoMo thất bại (mã lỗi: $resultCode)"
                            )
                        }
                    }
                    .onFailure {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isCancelled = true,  // ← vẫn đánh dấu dù update fail
                                errorMessage = "Thanh toán thất bại, vui lòng thử lại"
                            )
                        }
                    }
                return@launch
            }
            cancelPaymentRetryWorker(bookingId)
            // resultCode == 0 → success path (giữ nguyên)
            _uiState.update { it.copy(isLoading = true) }
            updateBookingStatusUseCase(bookingId, BookingStatus.PAID)
                .onSuccess {
                    savedStateHandle.remove<String>("bookingId")
                    savedStateHandle.remove<String>("momoPayUrl")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            bookingId = bookingId,
                            momoPayUrl = null
                        )
                    }
                }
                .onFailure {
                    observeBookingStatus(bookingId)
                }
        }
    }

    fun checkPendingPayment(bookingId: String) {
        if (_uiState.value.isLoading || _uiState.value.isSuccess || _uiState.value.isCancelled) return
        _uiState.update { it.copy(isLoading = true) }
        observeBookingStatus(bookingId)
    }

    // Thêm tag khi schedule
    private fun schedulePaymentRetry(bookingId: String) {
        val tag = "payment_retry_$bookingId"  // ✅ tag theo bookingId
        val workRequest = OneTimeWorkRequestBuilder<PaymentRetryWorker>()
            .setInputData(workDataOf("booking_id" to bookingId))
            .setInitialDelay(2, TimeUnit.MINUTES)
            .addTag(tag)  // ✅ thêm tag
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    // Thêm helper để cancel worker
    private fun cancelPaymentRetryWorker(bookingId: String) {
        WorkManager.getInstance(context)
            .cancelAllWorkByTag("payment_retry_$bookingId")
        Log.d("BookingViewModel", "Cancelled PaymentRetryWorker for $bookingId")
    }
}