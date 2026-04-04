package com.example.appointmentschedulingapp.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareSection() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val categories = listOf("Sức khỏe", "Xét nghiệm", "Tiêm chủng", "Nha khoa")

    Column(modifier = Modifier.padding(16.dp)) {
        // --- Tiêu đề phần ---
        Text("CHĂM SÓC", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Gói dịch vụ sức khỏe toàn diện", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))

        // --- Thanh lọc (Filter) ---
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(categories) { index, title ->
                FilterChip(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    label = { Text(title) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE3F2FD),
                        selectedLabelColor = Color(0xFF1976D2)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Danh sách Gói dịch vụ (Hardcoded) ---
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item { ServicePackageItem("Gói khám tổng quát Gold", "Bệnh viện Chợ Rẫy", "2.500.000đ") }
            item { ServicePackageItem("Xét nghiệm vi khuẩn HP", "Trung tâm Medic", "450.000đ") }
            item { ServicePackageItem("Tầm soát ung thư phổi", "BV Đại học Y Dược", "1.800.000đ") }
        }
    }
}

@Composable
fun ServicePackageItem(name: String, clinic: String, price: String) {
    Card(
        modifier = Modifier.width(160.dp), // Độ rộng cố định cho item trong hàng ngang
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // Ảnh đại diện gói (Tạm thời dùng Box màu)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(Color(0xFFBBDEFB)) // Màu xanh nhạt giả làm ảnh
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis, // Nếu tên dài quá thì hiện "..."
                    minLines = 2 // Đảm bảo các Card có chiều cao bằng nhau
                )

                Text(
                    text = clinic,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = price,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1976D2), // Màu xanh giá tiền
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}