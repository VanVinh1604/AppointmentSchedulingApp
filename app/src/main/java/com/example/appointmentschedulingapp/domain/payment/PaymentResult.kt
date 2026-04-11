package com.example.appointmentschedulingapp.domain.payment

sealed class PaymentResult {
    data class Redirect(
        val url: String,
        val bookingId: String
    ) : PaymentResult()

    data class Success(
        val bookingId: String
    ) : PaymentResult()

    data class Failure(val message: String) : PaymentResult()
}