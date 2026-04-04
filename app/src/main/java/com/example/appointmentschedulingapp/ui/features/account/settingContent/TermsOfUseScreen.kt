package com.example.appointmentschedulingapp.ui.features.account.settingContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfUseScreen(onBack: () -> Unit) {
    val primaryColor = Color(0xFF1976D2)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Quy định sử dụng", color = Color.White, style = MaterialTheme.typography.titleMedium)
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
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // --- TIÊU ĐỀ CHÍNH ---
            Text(
                text = "QUY ĐỊNH CHUNG",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryColor,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Chào mừng bạn đến với phần mềm MEDPRO - Đặt lịch khám bệnh. Vui lòng đọc kỹ các Quy Định Sử Dụng dưới đây...",
                lineHeight = 22.sp,
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- PHẦN ĐỊNH NGHĨA (Gom vào Card cho dễ nhìn) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    DefinitionItem("Chúng tôi:", "Công ty cổ phần ứng dụng PKH và các đơn vị liên kết.")
                    DefinitionItem("Dịch vụ:", "Các tính năng và tiện ích trên phần mềm.")
                    DefinitionItem("Bạn:", "Người dùng, bệnh nhân hoặc người được ủy quyền.")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- CÁC MỤC NỘI DUNG CHÍNH ---
            TermContentSection(
                title = "ĐĂNG NHẬP",
                bullets = listOf(
                    "Bạn phải đăng nhập để sử dụng đầy đủ dịch vụ.",
                    "Định danh tài khoản bằng số điện thoại di động.",
                    "Liên hệ ngay 1900 2115 nếu mất quyền kiểm soát số điện thoại."
                )
            )

            TermContentSection(
                title = "THÔNG TIN BỆNH NHÂN",
                bullets = listOf(
                    "Phải cung cấp thông tin chính xác trước khi đặt hẹn.",
                    "Đối với bệnh nhân cũ: Nhập mã số bệnh nhân (số hồ sơ).",
                    "Đối với bệnh nhân mới: Điền đầy đủ thông tin cá nhân chính xác.",
                    "Bệnh viện có quyền từ chối khám nếu thông tin đăng ký sai lệch."
                )
            )

            TermContentSection(
                title = "TIỀN VÀ PHÍ ĐĂNG KÝ",
                bullets = listOf(
                    "Tiền khám: Thay đổi tùy chuyên khoa (Tư vấn tâm lý: 500k, Tâm thần kinh: 250k...)",
                    "Phí tiện ích + TGTT: Phí hỗ trợ dịch vụ trực tuyến (không hoàn lại khi hủy phiếu).",
                    "Kiểm tra tổng tiền trước khi xác nhận thanh toán."
                )
            )

            TermContentSection(
                title = "QUY ĐỊNH HOÀN TIỀN",
                bullets = listOf(
                    "Chỉ hoàn lại tiền khám, không hoàn phí tiện ích.",
                    "Thời gian nhận tiền: 7 - 45 ngày tùy phương thức thanh toán.",
                    "Ngày làm việc không bao gồm Thứ 7, Chủ nhật và Ngày lễ."
                )
            )

            // --- PHẦN CAM KẾT CUỐI ---
            Spacer(modifier = Modifier.height(30.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TÔI ĐÃ ĐỌC VÀ ĐỒNG Ý VỚI TẤT CẢ CÁC QUY ĐỊNH TRÊN.",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = primaryColor,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Cập nhật gần nhất: 31/10/2022",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 100.dp)
            )
        }
    }
}

@Composable
fun DefinitionItem(label: String, desc: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                append("$label ")
            }
            append(desc)
        },
        fontSize = 14.sp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

// SỬA: Thay 'listOf<String>' thành 'List<String>'
@Composable
fun TermContentSection(title: String, bullets: List<String>) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        bullets.forEach { text ->
            Row(modifier = Modifier.padding(bottom = 6.dp)) {
                Text("• ", fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                Text(
                    text = text,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF444444)
                )
            }
        }
    }
}