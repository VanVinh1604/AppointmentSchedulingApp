package com.example.appointmentschedulingapp.ui.features.booking.steps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState

@Composable
fun BookingStep2Screen(
    uiState: BookingUiState,
    onPatientSelected: (String, String) -> Unit, // ID và Tên
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("CHỌN HỒ SƠ BỆNH NHÂN", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        // Giả sử có một danh sách hồ sơ (sau này lấy từ data)
        val patientList = listOf("Nguyễn Văn A", "Trần Thị B")

        LazyColumn {
            items(patientList) { name ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onPatientSelected("ID_123", name) },
                    border = if (uiState.patientName == name) BorderStroke(2.dp, Color.Blue) else null
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.Person, null)
                        Spacer(Modifier.width(12.dp))
                        Text(name)
                    }
                }
            }
        }

        // Nút thêm hồ sơ mới
        OutlinedButton(onClick = { /* Mở màn hình tạo hồ sơ */ }) {
            Icon(Icons.Default.Add, null)
            Text("Thêm hồ sơ bệnh nhân")
        }
    }
}