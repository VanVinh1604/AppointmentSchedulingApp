package com.example.appointmentschedulingapp.common

fun formatPrice(price: Long): String {
    if (price <= 0L) return "Miễn phí"
    return "%,d đ".format(price).replace(',', '.')
}