package com.example.appointmentschedulingapp.domain.payment.momoPayment

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object MomoCallbackBus {
    private val _events = MutableSharedFlow<Pair<String, Int>>(extraBufferCapacity = 1)
    val events: SharedFlow<Pair<String, Int>> = _events.asSharedFlow()

    fun emit(orderId: String, resultCode: Int) {
        _events.tryEmit(Pair(orderId, resultCode))
    }
}