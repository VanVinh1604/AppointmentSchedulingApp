package com.example.appointmentschedulingapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationForegroundService : Service() {   // ✅ android.app.Service

    companion object {
        const val CHANNEL_ID = "appointment_sync_channel"
        const val NOTIFICATION_ID = 1001

        fun start(context: Context) {
            val intent = Intent(context, NotificationForegroundService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, NotificationForegroundService::class.java))
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        // Android 14+ yêu cầu truyền serviceType — dùng ServiceCompat để tương thích
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            buildNotification(),
            0   // serviceType = 0 cho sync task thông thường
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY   // ✅ hằng số từ android.app.Service, không cần import thêm
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(): Notification =   // ✅ trả về android.app.Notification
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Đang đồng bộ lịch hẹn")
            .setContentText("Đang kết nối và cập nhật dữ liệu...")
            .setSmallIcon(android.R.drawable.ic_popup_sync)   // icon built-in của Android
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)   // không thể swipe dismiss khi đang chạy foreground
            .build()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Appointment Sync",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}