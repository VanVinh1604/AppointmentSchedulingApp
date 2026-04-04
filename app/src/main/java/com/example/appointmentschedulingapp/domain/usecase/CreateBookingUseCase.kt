package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(state: BookingUiState): Result<String> =
        withContext(dispatcher) {

            val clinic = state.selectedClinic
                ?: return@withContext Result.failure(Exception("Clinic null"))

            val booking = Booking(
                clinicId = clinic.id,
                clinicName = clinic.name,
                patientId = state.selectedPatientId,
                patientName = state.patientName,
                specialty = state.selectedSpecialty,
                service = state.selectedService,
                appointmentDate = state.selectedDate,
                appointmentTime = state.selectedTime
            )

            bookingRepository.createBooking(booking)
        }
}