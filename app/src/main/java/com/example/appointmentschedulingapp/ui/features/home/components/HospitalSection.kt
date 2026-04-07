package com.example.appointmentschedulingapp.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.ui.features.booking.ClinicViewModel

@Composable
fun HospitalSection(
    onViewAllClick: () -> Unit,
    onClinicClick: (String) -> Unit,
    onBookingClick: (Clinic) -> Unit,
    viewModel: ClinicViewModel = hiltViewModel()
) {
    // 1. Quan sát danh sách phòng khám từ ViewModel
    val clinics by viewModel.clinics.collectAsState()

    // 2. Chỉ lấy tối đa 15 items cho phần tiêu biểu ở Home để tối ưu hiệu năng
    val displayClinics = remember(clinics) {
        clinics.take(15)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "CƠ SỞ Y TẾ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Chuyển đến màn hình SelectClinicScreen (nơi hiện full danh sách)
            TextButton(
                onClick = onViewAllClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "Xem tất cả >>",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1976D2)
                )
            }
        }

        Text(
            "Nổi bật trong tháng",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 4. LazyRow render danh sách động
        LazyRow(
            contentPadding = PaddingValues(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayClinics, key = { it.id }) { clinic ->
                HospitalItem(
                    clinic = clinic,
                    onClick = { onClinicClick(clinic.id) },
                    onBookingClick = { onBookingClick(clinic) }
                )
            }
        }
    }
}

@Composable
fun HospitalItem(
    clinic: Clinic,
    onClick: () -> Unit,
    onBookingClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Hiển thị Banner/Image nếu có, nếu không dùng Placeholder
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Ở đây bạn có thể dùng AsyncImage (Coil) để load clinic.imageUrl
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tên bệnh viện (Dựa trên model Clinic)
            Text(
                text = clinic.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Địa chỉ rút gọn (Quận, Thành phố)
            Text(
                text = "${clinic.district}, ${clinic.city}",
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Đánh giá sao
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "⭐ ${clinic.rating}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFB300)
                )
                if (clinic.reviewsCount > 0) {
                    Text(
                        text = " (${clinic.reviewsCount})",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Button(
                onClick = onBookingClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Đặt khám ngay", fontSize = 12.sp)
            }
        }
    }
}