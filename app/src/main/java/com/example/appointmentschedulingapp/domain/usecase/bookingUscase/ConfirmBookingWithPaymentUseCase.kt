package com.example.appointmentschedulingapp.domain.usecase.bookingUscase

import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.model.ConfirmBookingRequest
import com.example.appointmentschedulingapp.domain.payment.PaymentProcessorFactory
import com.example.appointmentschedulingapp.domain.payment.PaymentResult
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus
import javax.inject.Inject

class ConfirmBookingWithPaymentUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val paymentProcessorFactory: PaymentProcessorFactory
) {
    suspend operator fun invoke(request: ConfirmBookingRequest): Result<PaymentResult> =
        runCatching {
            val bookingId = if (!request.existingBookingId.isNullOrEmpty()) {
                bookingRepository.updateBookingStatus(
                    request.existingBookingId,
                    BookingStatus.PENDING_PAYMENT
                ).getOrThrow()
                request.existingBookingId
            } else {
                bookingRepository.createBooking(
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
                        status = BookingStatus.PENDING_PAYMENT
                    )
                ).getOrThrow()
            }

            val processor = paymentProcessorFactory.get(request.paymentMethod)
            processor.processPayment(bookingId = bookingId, amount = request.consultationFee ?: 0L)
        }
}