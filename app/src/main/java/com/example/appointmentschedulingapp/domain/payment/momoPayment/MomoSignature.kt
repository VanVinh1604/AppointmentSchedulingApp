package com.example.appointmentschedulingapp.domain.payment.momoPayment

import android.util.Log
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object MomoSignature {
    private const val HMAC_SHA256 = "HmacSHA256"

    fun generate(
        partnerCode: String, accessKey: String, requestId: String,
        amount: String, orderId: String, orderInfo: String,
        redirectUrl: String, ipnUrl: String, extraData: String,
        requestType: String, secretKey: String
    ): String {
        val rawData = "accessKey=$accessKey" +
                "&amount=$amount" +
                "&extraData=$extraData" +
                "&ipnUrl=$ipnUrl" +
                "&orderId=$orderId" +
                "&orderInfo=$orderInfo" +
                "&partnerCode=$partnerCode" +
                "&redirectUrl=$redirectUrl" +
                "&requestId=$requestId" +
                "&requestType=$requestType"

        Log.d("MomoSignature", "RawData: $rawData")
        return hmacSHA256(secretKey, rawData)
    }

    private fun hmacSHA256(key: String, data: String): String {
        val secretKeySpec = SecretKeySpec(key.toByteArray(), HMAC_SHA256)
        val mac = Mac.getInstance(HMAC_SHA256)
        mac.init(secretKeySpec)
        return mac.doFinal(data.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}