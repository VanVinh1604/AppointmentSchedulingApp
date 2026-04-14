package com.example.appointmentschedulingapp

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppointmentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // ✅ Bật một lần duy nhất — Firebase tự queue writes khi offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}