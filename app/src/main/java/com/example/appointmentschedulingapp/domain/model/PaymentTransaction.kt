package com.example.appointmentschedulingapp.domain.model

data class PaymentTransaction(
    val transactionId: String,
    val appointmentId: String,
    val method: String, // MoMo, VNPAY, Thẻ [cite: 210]
    val amount: Double,
    val status: String
)