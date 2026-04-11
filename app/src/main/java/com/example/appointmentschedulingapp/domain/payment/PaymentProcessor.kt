package com.example.appointmentschedulingapp.domain.payment

interface PaymentProcessor {
    val methodId: String

    suspend fun processPayment(
        bookingId: String,
        amount: Long
    ): PaymentResult
}