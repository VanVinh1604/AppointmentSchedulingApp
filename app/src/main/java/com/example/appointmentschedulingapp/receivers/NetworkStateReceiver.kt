package com.example.appointmentschedulingapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import com.example.appointmentschedulingapp.di.WorkScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NetworkStateReceiver : BroadcastReceiver() {

    @Inject
    lateinit var workScheduler: WorkScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Log.d("NetworkStateReceiver", "onReceive action = $action")

        // ✅ Fix warning spoofed broadcast
        if (action != ConnectivityManager.CONNECTIVITY_ACTION) {
            Log.w("NetworkStateReceiver", "Ignored unexpected action")
            return
        }

        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        val isConnected =
            connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting == true

        Log.d("NetworkStateReceiver", "isConnected = $isConnected")

        if (isConnected) {
            Log.d("NetworkStateReceiver", "Internet restored -> schedule sync worker")
            workScheduler.scheduleSyncPendingBookings()
        }
    }
}