package com.example.appointmentschedulingapp.domain.usecase.bookingUscase

import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.model.ConfirmBookingRequest
import com.example.appointmentschedulingapp.domain.payment.PaymentProcessorFactory
import com.example.appointmentschedulingapp.domain.payment.PaymentResult
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus
import javax.inject.Inject

class ConfirmBookingWithPaymentUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val paymentProcessorFactory: PaymentProcessorFactory
) {
    suspend operator fun invoke(request: ConfirmBookingRequest): Result<PaymentResult> =
        runCatching {
            // ✅ Bước 1: Tạo booking → lấy bookingId thật
            val bookingId = bookingRepository.createBooking(
                Booking(
                    clinicId = request.clinicId,
                    clinicName = request.clinicName,
                    clinicAddress = request.clinicAddress,
                    patientId = request.patientId,
                    patientName = request.patientName,
                    specialty = request.specialty,
                    service = request.service,
                    appointmentDate = request.appointmentDate,
                    appointmentTime = request.appointmentTime,
                    consultationFee = request.consultationFee,
                    paymentMethod = request.paymentMethod,
                    status = BookingStatus.PENDING
                )
            ).getOrThrow() // ← bookingId thật từ Firebase push()

            // ✅ Bước 2: Dùng bookingId thật để tạo payment URL
            val processor = paymentProcessorFactory.get(request.paymentMethod)
            processor.processPayment(
                bookingId = bookingId,  // ← truyền đúng ID
                amount = request.consultationFee ?: 0L
            )
        }
}