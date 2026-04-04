package com.example.appointmentschedulingapp.ui.features.account.settingContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(onBack: () -> Unit) {
    val primaryColor = Color(0xFF1976D2)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Điều khoản dịch vụ", color = Color.White, style = MaterialTheme.typography.titleMedium)
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
                .background(Color(0xFFF5F5F5))
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // --- GIỚI THIỆU ---
            TOSCard(title = "1. GIỚI THIỆU", icon = Icons.Default.Info) {
                TOSBodyText("Chào mừng bạn đến với phần mềm MEDPRO - Đặt lịch khám bệnh. Vui lòng đọc kỹ các Điều Khoản Dịch Vụ này để biết được quyền lợi và nghĩa vụ hợp pháp của mình.")
                TOSBodyText("Bằng việc sử dụng Các Dịch Vụ, bạn xác nhận chấp nhận không rút lại các điều khoản này. Nếu không đồng ý, vui lòng ngừng sử dụng dịch vụ ngay lập tức.")
            }

            // --- TÀI KHOẢN ---
            TOSCard(title = "2. TÀI KHOẢN VÀ BẢO MẬT") {
                TOSBodyText("Bạn cần đăng nhập bằng số điện thoại (ID Người Dùng) và mã xác nhận để sử dụng dịch vụ.")
                TOSBulletItem("Bạn chịu trách nhiệm hoàn toàn đối với mọi hoạt động dưới tên tài khoản của mình.")
                TOSBulletItem("Thông báo ngay cho chúng tôi nếu phát hiện hành vi sử dụng trái phép tài khoản.")
                TOSBulletItem("Chúng tôi có quyền chấm dứt tài khoản nếu phát hiện hành vi gian lận hoặc vi phạm điều khoản.")
            }

            // --- PHÍ VÀ THANH TOÁN ---
            TOSCard(title = "3. CÁC KHOẢN PHÍ") {
                TOSBodyText("Bạn đồng ý thanh toán các khoản phí liên quan đến giao dịch đăng ký sử dụng dịch vụ.")
                TOSBulletItem("Giá dịch vụ chưa bao gồm thuế và phí đổi tiền (nếu có).")
                TOSBulletItem("Mọi giao dịch đã hoàn tất là cuối cùng và không hoàn lại, trừ khi có quy định khác trong Quy Định Sử Dụng.")
                TOSBulletItem("Chúng tôi có thể thay đổi giá dịch vụ bất kỳ lúc nào bằng cách đăng tải trên Phần mềm.")
            }

            // --- GIỚI HẠN SỬ DỤNG ---
            TOSCard(title = "4. GIỚI HẠN QUYỀN SỬ DỤNG") {
                TOSBodyText("Chúng tôi cấp quyền sử dụng có giới hạn vì mục đích cá nhân, phi thương mại.")
                TOSBulletItem("Không được sao chép, phát tán hoặc sửa đổi bất kỳ nội dung nào của Phần mềm.")
                TOSBulletItem("Không sử dụng robot, spider hoặc thiết bị tự động để theo dõi nội dung.")
            }

            // --- LOẠI TRỪ TRÁCH NHIỆM ---
            TOSCard(title = "5. LOẠI TRỪ TRÁCH NHIỆM") {
                TOSBodyText("CHÚNG TÔI KHÔNG ĐẢM BẢO DỊCH VỤ LUÔN KHẢ DỤNG, KHÔNG CÓ LỖI HOẶC KHÔNG CÓ VIRUS.")
                TOSBodyText("Trong mọi trường hợp, trách nhiệm pháp lý tối đa của chúng tôi đối với bạn chỉ giới hạn ở mức dưới 1.000.000 VNĐ.")
            }

            // --- HÀNH VI BỊ CẤM ---
            TOSCard(title = "6. CÁC HÀNH VI BỊ CẤM") {
                TOSBulletItem("Mạo danh cá nhân hoặc tổ chức khác.")
                TOSBulletItem("Tải lên nội dung bất hợp pháp, độc hại hoặc vi phạm bản quyền.")
                TOSBulletItem("Tìm cách giải mã, tấn công kỹ thuật vào hệ thống.")
                TOSBulletItem("Gửi thư rác, quảng cáo đa cấp không được phép.")
            }

            // --- ĐIỀU KHOẢN CHUNG ---
            TOSCard(title = "7. ĐIỀU KHOẢN CHUNG") {
                TOSBodyText("Điều khoản này được điều chỉnh bởi pháp luật Cộng Hòa Xã Hội Chủ Nghĩa Việt Nam.")
                TOSBodyText("Chúng tôi có quyền điều chỉnh điều khoản bất kỳ lúc nào bằng cách đăng tải bản cập nhật.")
            }

            // --- XÁC NHẬN ---
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                color = primaryColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "TÔI ĐÃ ĐỌC VÀ ĐỒNG Ý VỚI TẤT CẢ QUY ĐỊNH TRÊN.",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = primaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Text(
                text = "Cập nhật gần nhất: 31/11/2022",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 40.dp)
            )
        }
    }
}

@Composable
fun TOSCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, null, tint = Color(0xFF1976D2), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun TOSBodyText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        color = Color(0xFF555555),
        modifier = Modifier.padding(bottom = 8.dp),
        textAlign = TextAlign.Justify
    )
}

@Composable
fun TOSBulletItem(text: String) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        Text("• ", fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Text(text = text, fontSize = 14.sp, lineHeight = 20.sp, color = Color(0xFF555555))
    }
}