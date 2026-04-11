package com.example.appointmentschedulingapp.domain.usecase.bookingUscase

import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.payment.PaymentProcessorFactory
import com.example.appointmentschedulingapp.domain.payment.PaymentResult
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus
import javax.inject.Inject

// ConfirmBookingWithPaymentUseCase.kt
class ConfirmBookingWithPaymentUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val paymentProcessorFactory: PaymentProcessorFactory
) {
    suspend operator fun invoke(uiState: BookingUiState): Result<PaymentResult> =
        runCatching {
            // ✅ Bước 1: Tạo booking → lấy bookingId thật
            val bookingId = bookingRepository.createBooking(
                Booking(
                    clinicId = uiState.selectedClinic?.id ?: "",
                    clinicName = uiState.selectedClinic?.name ?: "",
                    clinicAddress = uiState.selectedClinic?.address ?: "",
                    patientId = uiState.selectedPatientId,
                    patientName = uiState.patientName,
                    specialty = uiState.selectedSpecialty,
                    service = uiState.selectedBookingType,
                    appointmentDate = uiState.selectedDate,
                    appointmentTime = uiState.selectedTime,
                    consultationFee = uiState.selectedClinic?.consultationFee ?: 0L,
                    paymentMethod = uiState.selectedPaymentMethod,
                    status = BookingStatus.PENDING
                )
            ).getOrThrow() // ← bookingId thật từ Firebase push()

            // ✅ Bước 2: Dùng bookingId thật để tạo payment URL
            val processor = paymentProcessorFactory.get(uiState.selectedPaymentMethod)
            processor.processPayment(
                bookingId = bookingId,  // ← truyền đúng ID
                amount = uiState.selectedClinic?.consultationFee ?: 0L
            )
        }
}