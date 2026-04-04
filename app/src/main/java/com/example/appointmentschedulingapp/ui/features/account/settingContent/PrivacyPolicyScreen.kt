package com.example.appointmentschedulingapp.ui.features.account.settingContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    val primaryColor = Color(0xFF1976D2)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Chính sách bảo mật", color = Color.White, style = MaterialTheme.typography.titleMedium)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = primaryColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA)) // Nền xám nhạt để các Card nổi bật lên
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // --- HEADER ---
            PrivacyHeaderSection(primaryColor)

            Spacer(modifier = Modifier.height(16.dp))

            // --- GIỚI THIỆU ---
            PrivacyCard(title = "1. GIỚI THIỆU") {
                PrivacyBodyText("Chào mừng bạn đến với phần mềm MEDPRO - Đặt lịch khám bệnh được vận hành bởi công ty cổ phần ứng dụng PKH. Chúng tôi nghiêm túc thực hiện trách nhiệm bảo mật thông tin theo pháp luật Việt Nam...")
                PrivacyBodyText("“Dữ liệu cá nhân” là dữ liệu về một cá nhân có thể định danh danh tính như tên, số CCCD, thông tin liên hệ...")
            }

            // --- THỜI ĐIỂM THU THẬP ---
            PrivacyCard(title = "2. KHI NÀO THU THẬP DỮ LIỆU?") {
                PrivacyBulletItem("Khi bạn đăng ký tài khoản hoặc sử dụng Dịch vụ.")
                PrivacyBulletItem("Khi bạn ký kết thỏa thuận hoặc cung cấp tài liệu tương tác.")
                PrivacyBulletItem("Thông qua các cuộc gọi (có ghi âm), email, mạng xã hội.")
                PrivacyBulletItem("Thông qua Cookie và các công nghệ tự động khi truy cập ứng dụng.")
            }

            // --- DỮ LIỆU THU THẬP ---
            PrivacyCard(title = "3. CHÚNG TÔI THU THẬP NHỮNG GÌ?") {
                Text("Hồ sơ cá nhân:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                PrivacyBodyText("Họ tên, giới tính, ngày sinh, địa chỉ, số điện thoại, email, ảnh BHYT/CCCD.")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Dữ liệu thanh toán:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                PrivacyBodyText("Mã giao dịch, số tiền, ngày giờ thực hiện qua các cổng trung gian.")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Dữ liệu Telemedicine:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                PrivacyBodyText("Nội dung chat, cuộc gọi video (Lưu trữ tối đa 30 ngày để xử lý sự cố).")
            }

            // --- MỤC ĐÍCH SỬ DỤNG ---
            PrivacyCard(title = "4. MỤC ĐÍCH SỬ DỤNG THÔNG TIN") {
                PrivacyBulletItem("Xử lý giao dịch và quản lý tài khoản người dùng.")
                PrivacyBulletItem("Xác minh danh tính và đảm bảo an toàn hệ thống.")
                PrivacyBulletItem("Liên hệ hỗ trợ, thông báo quản trị và tiếp thị (nếu có sự đồng ý).")
                PrivacyBulletItem("Nghiên cứu, phân tích để cải thiện trải nghiệm khách hàng.")
            }

            // --- BẢO VỆ THÔNG TIN ---
            PrivacyCard(title = "5. CÁCH CHÚNG TÔI BẢO VỆ DỮ LIỆU") {
                PrivacyBodyText("Dữ liệu được lưu trữ đằng sau các mạng bảo mật. Chúng tôi chỉ giữ lại dữ liệu khi còn mục đích hợp pháp hoặc theo yêu cầu pháp luật.")
                PrivacyBodyText("Người dùng có trách nhiệm tự bảo mật mật khẩu và mã OTP của mình.")
            }

            // --- QUYỀN CỦA NGƯỜI DÙNG ---
            PrivacyCard(title = "6. QUYỀN TRUY CẬP VÀ CHỈNH SỬA") {
                PrivacyBodyText("Bạn có quyền xem, cập nhật hoặc yêu cầu xóa dữ liệu cá nhân thông qua cài đặt tài khoản hoặc liên hệ email: info@medpro.vn.")
            }

            // --- CAM KẾT CUỐI ---
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "BẰNG VIỆC TIẾP TỤC SỬ DỤNG, BẠN ĐỒNG Ý VỚI CÁC ĐIỀU KHOẢN BẢO MẬT NÀY.",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = primaryColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Cập nhật gần nhất: 26/09/2024",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 40.dp)
            )
        }
    }
}

@Composable
fun PrivacyHeaderSection(color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Security, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Cam kết bảo mật của MEDPRO",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PrivacyCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1976D2)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = Color.LightGray)
            content()
        }
    }
}

@Composable
fun PrivacyBodyText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Color(0xFF444444),
        modifier = Modifier.padding(bottom = 8.dp),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun PrivacyBulletItem(text: String) {
    Row(modifier = Modifier.padding(bottom = 6.dp)) {
        Text("• ", fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Text(text = text, fontSize = 14.sp, lineHeight = 20.sp, color = Color(0xFF444444))
    }
}