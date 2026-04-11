package com.example.appointmentschedulingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.appointmentschedulingapp.domain.payment.MomoCallbackBus
import com.example.appointmentschedulingapp.ui.navigation.AppNavGraph
import com.example.appointmentschedulingapp.ui.navigation.MyBottomBar
import com.example.appointmentschedulingapp.ui.theme.AppointmentSchedulingAppTheme
import dagger.hilt.android.AndroidEntryPoint


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

                LaunchedEffect(Unit) {
                    intent?.let { currentIntent ->
                        Log.d(TAG, "Handling intent in LaunchedEffect")
                        handleDeepLink(currentIntent)
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

        Log.d(TAG, "Full URI: $data")

        // ✅ Bắt đúng scheme + host + path
        if (data.scheme == "appointmentschedulingapp" && data.host == "momo") {
            when (data.path) {
                "/success" -> {
                    val orderId = data.getQueryParameter("orderId")
                    Log.d(TAG, "MoMo SUCCESS callback, orderId: $orderId")
                    if (orderId != null) {
                        MomoCallbackBus.emit(orderId)
                    }
                }
                "/cancel" -> {
                    Log.d(TAG, "MoMo CANCEL callback")
                    // Có thể emit event cancel nếu cần
                }
            }
        } else {
            navController.handleDeepLink(intent)
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