package com.example.appointmentschedulingapp.domain.payment

import android.net.Uri
import java.util.UUID
import javax.inject.Inject

class MomoPaymentProcessor @Inject constructor() : PaymentProcessor {

    override val methodId: String = "MOMO"

    // MomoPaymentProcessor.kt
    override suspend fun processPayment(bookingId: String, amount: Long): PaymentResult {
        val requestId = UUID.randomUUID().toString()

        val deeplink = Uri.Builder()
            .scheme("momo")
            .authority("app")
            .appendQueryParameter("action", "gettoken")
            .appendQueryParameter("partnerCode", "MOMO")
            .appendQueryParameter("merchantname", "Clinic Booking")
            .appendQueryParameter("merchantcode", "MOMO")
            .appendQueryParameter("amount", amount.toString())
            .appendQueryParameter("orderId", bookingId)
            .appendQueryParameter("description", "Thanh toán lịch khám")
            .appendQueryParameter("requestId", requestId)
            .appendQueryParameter("appScheme", "appointmentschedulingapp")
            // ✅ Đúng format như app cũ: scheme://host/path
            .appendQueryParameter("returnUrl", "appointmentschedulingapp://momo/success?orderId=$bookingId")
            .build()
            .toString()

        return PaymentResult.Redirect(url = deeplink, bookingId = bookingId)
    }
}