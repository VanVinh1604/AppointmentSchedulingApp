package com.example.appointmentschedulingapp.domain.payment.momoPayment

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object MomoCallbackBus {
    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun emit(orderId: String) {
        _events.tryEmit(orderId)
    }
}