package com.example.appointmentschedulingapp.ui.features.booking

import com.example.appointmentschedulingapp.domain.model.Clinic

sealed class BookingEvent {
    data class LoadClinic(val id: String) : BookingEvent()
    data class SelectClinic(val clinic: Clinic) : BookingEvent()
    data class SelectService(val service: String) : BookingEvent()
    data class UpdateSpecialty(val specialty: String) : BookingEvent()
    data class SetStep(val step: Int) : BookingEvent()
    data class SelectDate(val date: String) : BookingEvent()
    data class SelectPaymentMethod(val method: String) : BookingEvent()
    data object PaymentSuccess : BookingEvent()
    data class PaymentFailure(val message: String = "Thanh toán không thành công") : BookingEvent()
    data class SelectTime(val time: String) : BookingEvent()
    data class SelectPatient(val id: String, val name: String) : BookingEvent()
    data object ConfirmBooking : BookingEvent()
}
