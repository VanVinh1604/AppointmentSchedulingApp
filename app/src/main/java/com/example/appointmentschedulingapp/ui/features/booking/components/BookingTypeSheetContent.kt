package com.example.appointmentschedulingapp.ui.features.booking.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.BedroomChild
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointmentschedulingapp.ui.features.booking.ServiceItemData

@Composable
fun BookingTypeSheetContent(
    clinicName: String,
    onClose: () -> Unit,
    onTypeSelected: (String) -> Unit,
) {
    val primaryColor = Color(0xFF1976D2)

    val bookingTypes = listOf(
        ServiceItemData("Khám dịch vụ", Icons.Default.MedicalServices),
        ServiceItemData("Khám theo bác sĩ", Icons.Default.Person),
        ServiceItemData("Khám ngoài giờ", Icons.Default.HistoryToggleOff),
        ServiceItemData("Phòng khám VIP - Doanh nhân", Icons.Default.Stars),
        ServiceItemData("Khám thường", Icons.Default.HealthAndSafety),
        ServiceItemData("Tái khám nội trú", Icons.Default.BedroomChild)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp) // Padding 20 như bạn yêu cầu
            .padding(bottom = 32.dp)
    ) {
        // --- TOPBAR CỦA SHEET ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
            Text(
                text = "CHỌN HÌNH THỨC ĐẶT KHÁM",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
            Spacer(modifier = Modifier.width(48.dp)) // Tạo khoảng trống cân bằng với nút X
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TÊN BỆNH VIỆN ---
        Text(
            text = clinicName,
            color = primaryColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- DANH SÁCH HÌNH THỨC (MAX FILL) ---
        bookingTypes.forEach { type ->
            OutlinedButton(
                onClick = { onTypeSelected(type.title) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = type.icon,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = type.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.LightGray
                    )
                }
            }
        }
    }
}