package com.example.appointmentschedulingapp.domain.payment

import android.util.Log
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handler để xử lý callback từ MoMo và quản lý trạng thái thanh toán
 */
@Singleton
class MomoPaymentHandler @Inject constructor() {

    private companion object {
        const val TAG = "MomoPaymentHandler"
        const val POLLING_INTERVAL = 2000L // 2 giây
        const val MAX_POLLING_ATTEMPTS = 15 // 30 giây tối đa
    }

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    /**
     * Ghi nhận khi MoMo callback trở về
     * Được gọi khi deep link được xử lý trong MainActivity
     */
    fun onMomoCallbackReceived(orderId: String) {
        Log.d(TAG, "MoMo callback received for orderId: $orderId")
    }

    /**
     * Hủy tất cả coroutines khi app destroy
     */
    fun cleanup() {
        scope.cancel()
    }
}

