// domain/payment/MomoPaymentProcessor.kt
package com.example.appointmentschedulingapp.domain.payment.momoPayment

import android.util.Base64
import android.util.Log
import com.example.appointmentschedulingapp.domain.payment.PaymentProcessor
import com.example.appointmentschedulingapp.domain.payment.PaymentResult
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MomoPaymentProcessor @Inject constructor() : PaymentProcessor {

    override val methodId: String = "MOMO"

    companion object {
        private const val PARTNER_CODE = "MOMO"
        private const val ACCESS_KEY = "F8BBA842ECF85"
        private const val SECRET_KEY = "K951B6PE1waDMi640xX08PD3vg6EkVlz"
        private const val REDIRECT_URL = "appointmentschedulingapp://momo/success"
        private const val IPN_URL = "https://webhook.site/your-test-url" // thay bằng URL thật nếu có
        private const val CREATE_ORDER_URL = "https://test-payment.momo.vn/v2/gateway/api/create"
        private const val TAG = "MomoPaymentProcessor"
    }

    override suspend fun processPayment(bookingId: String, amount: Long): PaymentResult {
        return try {
            val payUrl = createMomoOrder(bookingId, amount)
            if (payUrl != null) {
                Log.d(TAG, "MoMo payUrl: $payUrl")
                PaymentResult.Redirect(url = payUrl, bookingId = bookingId)
            } else {
                PaymentResult.Failure("Không thể tạo đơn hàng MoMo")
            }
        } catch (e: Exception) {
            Log.e(TAG, "MoMo payment error: ${e.message}")
            PaymentResult.Failure(e.message ?: "Lỗi không xác định")
        }
    }

    private suspend fun createMomoOrder(bookingId: String, amount: Long): String? =
        suspendCoroutine { continuation ->
            val requestId = UUID.randomUUID().toString()

            // ✅ Tạo orderId hợp lệ cho MoMo (chỉ chứa chữ và số, không bắt đầu bằng -)
            // Dùng timestamp + 6 ký tự cuối của bookingId (bỏ ký tự đặc biệt)
            val cleanBookingId = bookingId.replace(Regex("[^0-9a-zA-Z]"), "")
            val momoOrderId = "APPT${System.currentTimeMillis()}${cleanBookingId.takeLast(6)}"

            val orderInfo = "Thanh toán lịch khám"

            // ✅ Encode bookingId thật vào extraData để lấy lại sau callback
            val extraData = Base64.encodeToString(
                bookingId.toByteArray(),
                Base64.NO_WRAP
            )

            val signature = MomoSignature.generate(
                partnerCode = PARTNER_CODE,
                accessKey = ACCESS_KEY,
                requestId = requestId,
                amount = amount.toString(),
                orderId = momoOrderId,      // ✅ dùng momoOrderId thay vì bookingId
                orderInfo = orderInfo,
                redirectUrl = REDIRECT_URL,
                ipnUrl = IPN_URL,
                extraData = extraData,
                requestType = "captureWallet",
                secretKey = SECRET_KEY
            )

            val json = JSONObject().apply {
                put("partnerCode", PARTNER_CODE)
                put("accessKey", ACCESS_KEY)
                put("requestId", requestId)
                put("amount", amount.toString())
                put("orderId", momoOrderId)  // ✅
                put("orderInfo", orderInfo)
                put("redirectUrl", REDIRECT_URL)
                put("ipnUrl", IPN_URL)
                put("extraData", extraData)
                put("requestType", "captureWallet")
                put("lang", "en")
                put("signature", signature)
            }
            Log.d(TAG, "MoMo request: $json")

            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(CREATE_ORDER_URL)
                .post(body)
                .build()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(TAG, "Request failed: ${e.message}")
                    continuation.resume(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    val bodyStr = response.body?.string()
                    Log.d(TAG, "HTTP ${response.code} - Body: $bodyStr")
                    val payUrl = bodyStr?.let { JSONObject(it).optString("payUrl", null) }
                    continuation.resume(payUrl)
                }
            })
        }
}