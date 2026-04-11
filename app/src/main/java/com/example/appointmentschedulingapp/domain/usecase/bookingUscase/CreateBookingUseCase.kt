package com.example.appointmentschedulingapp.domain.usecase.bookingUscase

import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState

import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
) {
    suspend operator fun invoke(state: BookingUiState): Result<String> {

        val clinic = state.selectedClinic
            ?: return Result.failure(Exception("Clinic is null"))

        val booking = Booking(
            clinicId = clinic.id,
            clinicName = clinic.name,
            patientId = state.selectedPatientId,
            patientName = state.patientName,
            specialty = state.selectedSpecialty,
            service = state.selectedBookingType,
            appointmentDate = state.selectedDate,
            appointmentTime = state.selectedTime,
            paymentMethod = state.selectedPaymentMethod

        )

        return bookingRepository.createBooking(booking)
    }

}