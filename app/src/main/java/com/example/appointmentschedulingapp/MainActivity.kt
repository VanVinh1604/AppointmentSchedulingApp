package com.example.appointmentschedulingapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.appointmentschedulingapp.domain.payment.momoPayment.MomoCallbackBus
import com.example.appointmentschedulingapp.ui.navigation.AppNavGraph
import com.example.appointmentschedulingapp.ui.navigation.MyBottomBar
import com.example.appointmentschedulingapp.ui.theme.AppointmentSchedulingAppTheme
import com.example.appointmentschedulingapp.workers.AppointmentReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


// File: MainActivity.kt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private companion object {
        const val TAG = "MainActivity"
    }

    // 1. Khai báo property để dùng chung trong toàn bộ class
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Log intent ngay khi onCreate
        Log.d(TAG, "onCreate called with intent action: ${intent?.action}")
        Log.d(TAG, "onCreate intent data: ${intent?.data}")
        Log.d(TAG, "onCreate intent extras: ${intent?.extras}")


        setContent {
            AppointmentSchedulingAppTheme {
                // 2. GÁN trực tiếp vào biến toàn cục thay vì khai báo 'val' mới
                navController = rememberNavController()

                val sessionViewModel: UserSessionViewModel = hiltViewModel()
                val session by sessionViewModel.session.collectAsState()

                RequestNotificationPermission()

                Scaffold(
                    bottomBar = { MyBottomBar(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavGraph(
                            navController = navController,
                            session = session,
                            onLogout = {
                                sessionViewModel.logout {
                                    navController.navigate("auth")
                                }
                            }
                        )
                    }
                }

                // MainActivity.kt - thay LaunchedEffect(Unit) bằng cách check action
                LaunchedEffect(Unit) {
                    intent?.let { currentIntent ->
                        // ✅ Chỉ xử lý nếu là deep link thật, không phải MAIN intent
                        if (currentIntent.action == Intent.ACTION_VIEW && currentIntent.data != null) {
                            Log.d(TAG, "Handling deep link intent in LaunchedEffect")
                            handleDeepLink(currentIntent)
                        }
                    }
                }

            }
        }
    }

    // 3. Xử lý onNewIntent khi app đã chạy và nhận intent mới (từ MoMo callback)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Log.d(TAG, "onNewIntent called with action: ${intent.action}")
        Log.d(TAG, "onNewIntent data: ${intent.data}")
        Log.d(TAG, "onNewIntent extras: ${intent.extras}")

        setIntent(intent)
        handleDeepLink(intent)
    }

    /**
     * Xử lý deep link từ MoMo callback
     * Deep link format: appointmentschedulingapp://payment?orderId=xxxxx
     */
    private fun handleDeepLink(intent: Intent) {
        val data = intent.data ?: return
        Log.d(TAG, "Deep link: $data")

        if (data.scheme == "appointmentschedulingapp" && data.host == "momo") {
            when (data.path) {
                "/success" -> {
                    // ✅ MoMo luôn callback về /success, phải đọc resultCode thật
                    val resultCode = data.getQueryParameter("resultCode")?.toIntOrNull() ?: -1
                    val orderId = data.getQueryParameter("orderId") ?: return

                    // Decode bookingId từ extraData
                    val extraData = data.getQueryParameter("extraData")
                    val bookingId = if (!extraData.isNullOrEmpty()) {
                        try {
                            String(android.util.Base64.decode(extraData, android.util.Base64.NO_WRAP))
                        } catch (e: Exception) {
                            null
                        }
                    } else null

                    Log.d(TAG, "MoMo callback resultCode=$resultCode, bookingId=$bookingId, orderId=$orderId")

                    if (resultCode == 0) {
                        // ✅ Thành công thật sự
                        val finalBookingId = bookingId ?: orderId
                        Log.d(TAG, "MoMo SUCCESS, bookingId: $finalBookingId")
                        MomoCallbackBus.emit(finalBookingId, resultCode = 0)
                    } else {
                        // ✅ Thất bại hoặc bị hủy (resultCode 1006, 1005, v.v.)
                        Log.d(TAG, "MoMo FAILED/CANCELLED via /success path, resultCode=$resultCode")
                        MomoCallbackBus.emit(orderId, resultCode = resultCode)
                    }
                }

                "/cancel" -> {
                    Log.d(TAG, "MoMo CANCEL path")
                    val orderId = data.getQueryParameter("orderId") ?: return
                    MomoCallbackBus.emit(orderId, resultCode = 9000)
                }

                "/failed" -> {
                    val orderId = data.getQueryParameter("orderId") ?: return
                    val resultCode = data.getQueryParameter("resultCode")?.toIntOrNull() ?: -1
                    Log.d(TAG, "MoMo FAILED path, resultCode=$resultCode")
                    MomoCallbackBus.emit(orderId, resultCode)
                }
            }
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val context = androidx.compose.ui.platform.LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    LaunchedEffect(Unit) {
        val granted = androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!granted) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppointmentSchedulingAppTheme {
        Greeting("Android")
    }
}