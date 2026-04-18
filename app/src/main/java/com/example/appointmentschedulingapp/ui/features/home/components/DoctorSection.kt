package com.example.appointmentschedulingapp.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.appointmentschedulingapp.domain.model.Doctor

@Composable
fun DoctorSection(
    doctors: List<Doctor>, // Nhận danh sách từ HomeScreen
    onViewAllClick: () -> Unit = {},
    onDoctorClick: (String) -> Unit = {}
) {
    val limitedDoctors = remember(doctors) {
        doctors.take(10)
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Text("BÁC SĨ TƯ VẤN", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("Xem tất cả >>", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1976D2))
        }


        Text("Khám bệnh qua video", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp) // Khoảng cách giữa các item
        ) {
            // Sử dụng danh sách đã được giới hạn
            items(limitedDoctors, key = { it.id }) { doctor ->
                DoctorItem(
                    doctor = doctor,
                    onClick = { onDoctorClick(doctor.id) }
                )
            }
        }
    }
}

@Composable
fun DoctorItem(
    doctor: Doctor,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(170.dp) // Chỉ dùng 1 Card duy nhất
            .padding(end = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) { // Dùng Column để xếp chồng
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(doctor.imageUrl)
                    .allowHardware(false) // 🔥 QUAN TRỌNG NHẤT
                    .crossfade(true)
                    .build(),
                contentDescription = doctor.fullName,
                modifier = Modifier
                    .height(110.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            // SỬ DỤNG DỮ LIỆU TỪ OBJECT DOCTOR Ở ĐÂY
            Text("⭐ ${doctor.rating}", fontSize = 12.sp, color = Color(0xFFFFB300))

            Text(
                text = doctor.fullName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1
            )

            Text(
                text = doctor.title, // Thay cho "Tim mạch" hoặc dùng mapping khoa
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Text(
                text = "200.000đ",
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.Bold
            )
        }
    }
}