//package com.example.appointmentschedulingapp.receivers
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import androidx.work.OneTimeWorkRequestBuilder
//import androidx.work.WorkManager
//import com.example.appointmentschedulingapp.workers.SyncPendingBookingsWorker
//
//class PowerConnectionReceiver : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent?) {
//        val work = OneTimeWorkRequestBuilder<SyncPendingBookingsWorker>()
//            .build()
//
//        WorkManager.getInstance(context).enqueue(work)
//    }
//}