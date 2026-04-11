package com.example.appointmentschedulingapp.domain.payment

import javax.inject.Inject

class CashPaymentProcessor @Inject constructor() : PaymentProcessor {

    override val methodId: String = "CASH"

    override suspend fun processPayment(
        bookingId: String,
        amount: Long
    ): PaymentResult {
        return PaymentResult.Success(bookingId)
    }
}