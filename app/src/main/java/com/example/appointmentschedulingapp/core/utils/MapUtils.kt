package com.example.appointmentschedulingapp.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object MapUtils {

    /**
     * Mở Google Maps app để tìm đường.
     * Fallback về trình duyệt nếu không cài Maps.
     */
    fun openNavigation(context: Context, latitude: Double, longitude: Double) {
        val googleMapsIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("google.navigation:q=$latitude,$longitude&mode=d")
        ).apply {
            setPackage("com.google.android.apps.maps")
        }

        val fallbackIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude")
        )

        val intent = if (googleMapsIntent.resolveActivity(context.packageManager) != null) {
            googleMapsIntent
        } else {
            fallbackIntent
        }

        context.startActivity(intent)
    }

    /**
     * Mở Google Maps để xem vị trí (không navigate).
     */
    fun openLocation(context: Context, latitude: Double, longitude: Double, label: String = "") {
        val uri = if (label.isNotBlank()) {
            Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(${Uri.encode(label)})")
        } else {
            Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        }

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }

        val fallback = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://maps.google.com/?q=$latitude,$longitude")
        )

        context.startActivity(
            if (intent.resolveActivity(context.packageManager) != null) intent else fallback
        )
    }
}