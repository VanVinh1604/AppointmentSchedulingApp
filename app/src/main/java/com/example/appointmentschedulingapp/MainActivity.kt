package com.example.appointmentschedulingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.appointmentschedulingapp.ui.navigation.AppNavGraph
import com.example.appointmentschedulingapp.ui.navigation.MyBottomBar
import com.example.appointmentschedulingapp.ui.theme.AppointmentSchedulingAppTheme
import dagger.hilt.android.AndroidEntryPoint


// File: MainActivity.kt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppointmentSchedulingAppTheme { // Theme của app bạn
                // 1. Khởi tạo Controller ở cấp cao nhất
                val navController = rememberNavController()

                // 2. Sử dụng Scaffold để làm khung có BottomBar [cite: 14]
                Scaffold(
                    bottomBar = {
                        // Đây là nơi bạn sẽ đặt BottomNavigationBar (tạo sau)
                        MyBottomBar(navController)
//                        Text("Bottom Bar đây nè") // test thử

                    }
                ) { innerPadding ->
                    // 3. Gọi NavGraph và truyền padding vào để không bị đè bởi BottomBar
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavGraph(navController = navController)
                    }


                }
            }
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