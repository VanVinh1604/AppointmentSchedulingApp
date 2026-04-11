package com.example.appointmentschedulingapp.domain.payment

import javax.inject.Inject

class PaymentProcessorFactory @Inject constructor(
    processors: Set<@JvmSuppressWildcards PaymentProcessor>
) {
    private val processorMap = processors.associateBy { it.methodId }

    fun get(methodId: String): PaymentProcessor {
        return processorMap[methodId]
            ?: error("Unsupported payment method: $methodId")
    }
}