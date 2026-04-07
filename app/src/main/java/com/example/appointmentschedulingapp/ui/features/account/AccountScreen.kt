package com.example.appointmentschedulingapp.ui.features.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(onNavigateToTerms: () -> Unit, onNavigateToPrivacy: () -> Unit,
                  onNavigateToService: () -> Unit,onNavigateToAuth:() -> Unit,
                  isLoggedIn: Boolean = false,
                  phoneNumber: String = "",
                  onLogout: () -> Unit = {}
) {
    val primaryColor = Color(0xFF1976D2)
    val dividerColor = Color(0xFFEEEEEE)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            // TopAppBar đồng nhất với các trang khác
            CenterAlignedTopAppBar(
                title = {
                    Text("Trang cá nhân", color = Color.White, style = MaterialTheme.typography.titleMedium)
                },
                navigationIcon = {
                    IconButton(onClick = { /* Quay lại */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = primaryColor // Cùng màu với Header phía dưới
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            // --- 1. HEADER (Phần xanh tiếp nối TopAppBar) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // Giảm chiều cao xuống một chút vì đã có TopAppBar chiếm một phần
                    .height(180.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(primaryColor),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    // Vòng tròn chứa Icon/Avatar
                    Surface(
                        modifier = Modifier.size(90.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isLoggedIn && phoneNumber.isNotEmpty())
                            phoneNumber
                        else
                            "Khách",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    // ✅ Hiển thị "Đăng xuất" hay "Đăng nhập / Đăng ký" tuỳ trạng thái
                    if (isLoggedIn) {
                        Text(
                            "Đăng xuất",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onLogout() }
                                .padding(4.dp)
                        )
                    } else {
                        Text(
                            "Đăng nhập / Đăng ký",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onNavigateToAuth() }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // --- 2. DANH SÁCH CÀI ĐẶT ---
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Điều khoản và quy định",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                SettingRow("Quy định sử dụng", Icons.Default.Gavel,onClick = onNavigateToTerms)
                SettingRow("Chính sách bảo mật", Icons.Default.PrivacyTip,onClick = onNavigateToPrivacy)
                SettingRow("Điều khoản dịch vụ", Icons.Default.Description, onClick = onNavigateToService)

                HorizontalDivider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                SettingRow("Xem/Lưu thông tin sức khỏe", Icons.Default.HealthAndSafety)

                HorizontalDivider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                SettingRow("Hỗ trợ tư vấn/Đặt khám 190000009", Icons.Default.Phone)

                HorizontalDivider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                SettingRow("Đánh giá ứng dụng", Icons.Default.Star)
                SettingRow("Tham gia cộng đồng App", Icons.Default.Groups)
                SettingRow("Chia sẻ ứng dụng", Icons.Default.Share)

                HorizontalDivider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                SettingRow("Một số câu hỏi thường gặp", Icons.Default.QuestionAnswer)

                HorizontalDivider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                // Dòng ngôn ngữ
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Language, null, tint = primaryColor, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Chuyển đổi ngôn ngữ", modifier = Modifier.weight(1f), fontSize = 14.sp)
                    Text("Tiếng Việt", color = Color.Gray, fontSize = 12.sp)
                    Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
                }
            }

            // --- 3. FOOTER (Thông tin App) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Android,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = Color(0xFF3DDC84)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("236/29.15 Bình Thạnh - P.16 - Q.Bình Thạnh - TPHCM", fontSize = 10.sp, color = primaryColor)
                        Text("Email: appNew@newApp.com", fontSize = 10.sp, color = primaryColor)
                        Text("Hotline: 1900 1234", fontSize = 10.sp, color = primaryColor)
                        Text("Phiên bản: 1.0.0", fontSize = 10.sp, color = primaryColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
            // Khoảng cách dự phòng cho Bottom Navigation
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SettingRow(title: String, icon: ImageVector,onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF1976D2),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, modifier = Modifier.weight(1f), fontSize = 14.sp)
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}