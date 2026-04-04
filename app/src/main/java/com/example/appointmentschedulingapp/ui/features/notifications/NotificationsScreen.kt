package com.example.appointmentschedulingapp.ui.features.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    val primaryColor = Color(0xFF1976D2)
    val lightBlueBg = Color(0xFFE3F2FD)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Thông báo", color = Color.White, style = MaterialTheme.typography.titleMedium)
                },
                navigationIcon = {
                    IconButton(onClick = { /* Quay lại */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                // Thêm một icon phụ bên phải để đánh dấu đã đọc hết (nếu cần)
                actions = {
                    IconButton(onClick = { /* Đánh dấu đã đọc */ }) {
                        Icon(Icons.Default.MarkEmailRead, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = primaryColor)
            )
        }
    ) { paddingValues ->
        // Sử dụng Box để căn giữa nội dung theo cả hai chiều dễ dàng nhất
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            contentAlignment = Alignment.Center // Căn giữa tuyệt đối (Vertical & Horizontal)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon hình bức thư trong vòng tròn xanh mờ
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    color = lightBlueBg
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Email, // Hình bức thư
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Dòng chữ thông báo
                Text(
                    text = "Bạn chưa có thông báo nào",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Các tin nhắn và cập nhật sẽ hiện ở đây.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )

                // Khoảng cách dự phòng cho Bottom Nav
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}