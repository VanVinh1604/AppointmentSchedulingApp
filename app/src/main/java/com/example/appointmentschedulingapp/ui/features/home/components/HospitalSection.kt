package com.example.appointmentschedulingapp.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HospitalSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("CƠ SỞ Y TẾ", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Xem tất cả >>", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1976D2))
        }

        Text("Nổi bật trong tháng", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow {
            items(5) { HospitalItem() }
        }
    }
}

@Composable
fun HospitalItem() {
    Card(
        modifier = Modifier.width(220.dp).padding(end = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier.height(90.dp).fillMaxWidth().background(Color.LightGray, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Bệnh viện Đa khoa Quốc tế A", fontWeight = FontWeight.Bold, maxLines = 1)
            Text("Quận 1, TP.HCM", fontSize = 12.sp, color = Color.Gray)
            Text("⭐ 4.5", fontSize = 12.sp, fontWeight = FontWeight.Bold)

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Đặt khám ngay", fontSize = 12.sp)
            }
        }
    }
}