package com.example.appointmentschedulingapp.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DoctorSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Text("BÁC SĨ TƯ VẤN", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("Xem tất cả >>", style = MaterialTheme.typography.bodySmall, color = Color(0xFF1976D2))
        }


        Text("Khám bệnh qua video", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow {
            items(5) { DoctorItem() }
        }
    }
}

@Composable
fun DoctorItem() {
    Card(
        modifier = Modifier.width(180.dp).padding(end = 12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.height(100.dp).fillMaxWidth().background(Color.LightGray, RoundedCornerShape(8.dp)))
            Spacer(modifier = Modifier.height(8.dp))
            Text("⭐ 4.8", fontSize = 12.sp)
            Text("BS. Nguyễn Văn A", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Tim mạch", fontSize = 12.sp, color = Color.Gray)
            Text("200.000đ", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
        }
    }
}