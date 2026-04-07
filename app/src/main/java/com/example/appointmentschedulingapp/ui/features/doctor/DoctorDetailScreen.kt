package com.example.appointmentschedulingapp.ui.features.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDetailScreen(
    doctorId: String,
    viewModel: DoctorDetailViewModel,
    onBack: () -> Unit,
    onBookNow: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val primaryColor = Color(0xFF1976D2)

    LaunchedEffect(doctorId) {
        viewModel.loadDoctor(doctorId)
    }

    Scaffold(
        bottomBar = {
            uiState.doctor?.let { doctor ->
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 12.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onBookNow(doctor.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text("ĐẶT LỊCH HẸN NGAY", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    }
                }
            }
        }
    ) { padding ->
        // Nội dung chính
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.doctor != null -> {
                    val doctor = uiState.doctor!!

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 1. Image Header với bo góc dưới
                        Box(modifier = Modifier.height(300.dp)) {
                            AsyncImage(
                                model = doctor.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                                contentScale = ContentScale.Crop
                            )

                            // Nút Back đè lên ảnh (Góc trái)
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .padding(top = 40.dp, start = 16.dp)
                                    .background(Color.White.copy(alpha = 0.7f), CircleShape)
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = null)
                            }

                            // 🔥 THÊM: Nút Yêu thích đè lên ảnh (Góc phải)
                            IconButton(
                                onClick = { /* TODO: Xử lý yêu thích */ },
                                modifier = Modifier
                                    .align(Alignment.TopEnd) // Căn góc trên bên phải
                                    .padding(top = 40.dp, end = 16.dp)
                                    .background(Color.White.copy(alpha = 0.7f), CircleShape)
                            ) {
                                // Sử dụng FavoriteBorder (trái tim rỗng) cho trạng thái chưa thích
                                Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Red)
                            }
                        }

                        Column(modifier = Modifier.padding(20.dp)) {
                            // 2. Tên và Danh hiệu
                            Text(
                                text = doctor.fullName,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = doctor.title,
                                color = primaryColor,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 🔥 THÊM: Quick Action Buttons (Hàng các nút tương tác nhanh)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround // Trải đều các nút
                            ) {
                                // Nút Nhắn tin
                                ActionButton(
                                    icon = Icons.Default.ChatBubbleOutline,
                                    label = "Nhắn tin",
                                    onClick = { /* TODO: Xử lý nhắn tin */ }
                                )
                                // Nút Gọi điện
                                ActionButton(
                                    icon = Icons.Default.Call,
                                    label = "Gọi điện",
                                    onClick = { /* TODO: Xử lý gọi điện */ }
                                )
                                // Nút Chia sẻ
                                ActionButton(
                                    icon = Icons.Default.Share,
                                    label = "Chia sẻ",
                                    onClick = { /* TODO: Xử lý chia sẻ */ }
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // 3. Quick Stats Row (Rating, Exp, Patients) - Giữ nguyên
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                QuickInfoItem("Đánh giá", "⭐ ${doctor.rating}")
                                QuickInfoItem("Kinh nghiệm", "8+ Năm")
                                QuickInfoItem("Bệnh nhân", "1000+")
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // 4. Section Giới thiệu - Giữ nguyên
                            Text(
                                "Giới thiệu chi tiết",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = doctor.biography.ifBlank { "Đang cập nhật thông tin giới thiệu cho bác sĩ này." },
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.DarkGray,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // 5. Section Chuyên khoa & Công tác - Giữ nguyên
                            InfoSectionCard(
                                title = "Cơ sở làm việc",
                                content = "Bệnh viện Đa khoa Trung tâm",
                                icon = Icons.Default.Business
                            )

                            Spacer(modifier = Modifier.height(24.dp))

// 6. Section Đánh giá từ bệnh nhân
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Đánh giá & Nhận xét",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Xem tất cả",
                                    color = primaryColor,
                                    fontSize = 14.sp,
                                    modifier = Modifier.clickable { /* Xem toàn bộ review */ }
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

// Demo dữ liệu (Sau này bạn sẽ lấy List<Review> từ Firebase dựa trên doctorId)
                            ReviewItem(
                                userName = "Nguyễn Minh Anh",
                                rating = 5,
                                date = "20/03/2024",
                                comment = "Bác sĩ tư vấn rất nhiệt tình, giải thích cặn kẽ tình trạng bệnh. Tôi cảm thấy rất an tâm."
                            )

                            ReviewItem(
                                userName = "Trần Hoàng Nam",
                                rating = 4,
                                date = "15/03/2024",
                                comment = "Phòng khám sạch sẽ, bác sĩ chuyên môn cao nhưng đợi hơi lâu một chút."
                            )

                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }
        }
    }
}

// 🔥 Component mới: ActionButton (Cho Nhắn tin, Gọi điện, Chia sẻ)
@Composable
fun ActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() } // Clickable để sau này dùng tới
            .padding(8.dp)
            .width(80.dp) // Độ rộng cố định để các text đều nhau
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFFE3F2FD), CircleShape), // Nền xanh nhạt
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color(0xFF1976D2), modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun QuickInfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFFF5F7FA), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .width(80.dp)
    ) {
        Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun InfoSectionCard(title: String, content: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Color(0xFF1976D2))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(content, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ReviewItem(
    userName: String,
    rating: Int,
    date: String,
    comment: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(userName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(date, fontSize = 12.sp, color = Color.Gray)
        }

        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            repeat(5) { index ->
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (index < rating) Color(0xFFFFB300) else Color.LightGray
                )
            }
        }

        Text(
            text = comment,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            lineHeight = 20.sp
        )

        HorizontalDivider(modifier = Modifier.padding(top = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
    }
}