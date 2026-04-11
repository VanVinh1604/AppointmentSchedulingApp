//package com.example.appointmentschedulingapp.ui.features.shared
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.QrCode
//import androidx.compose.material.icons.outlined.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.unit.dp
//import com.example.appointmentschedulingapp.ui.features.tickets.components.DetailRow
//import com.example.appointmentschedulingapp.ui.features.tickets.components.DetailSection
//import com.example.appointmentschedulingapp.ui.features.tickets.formatPrice
//
//@Composable
//fun AppointmentReceiptContent(
//    clinicName: String,
//    specialty: String,
//    patientName: String,
//    appointmentDate: String,
//    appointmentTime: String,
//    fee: Long,
//    bookingId: String,
//    statusText: String? = null,
//    showQr: Boolean = false
//) {
//    val colorScheme = MaterialTheme.colorScheme
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(colorScheme.surfaceVariant.copy(alpha = 0.2f))
//            .verticalScroll(rememberScrollState())
//            .padding(16.dp)
//    ) {
//        Card(shape = RoundedCornerShape(20.dp)) {
//            Column(Modifier.padding(16.dp)) {
//                Text(
//                    clinicName,
//                    style = MaterialTheme.typography.titleMedium
//                )
//
//                Spacer(Modifier.height(6.dp))
//
//                Text(
//                    specialty,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//
//                statusText?.let {
//                    Spacer(Modifier.height(12.dp))
//                    AssistChip(
//                        onClick = {},
//                        label = { Text(it) }
//                    )
//                }
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        DetailSection("Thông tin lịch khám") {
//            DetailRow(Icons.Outlined.CalendarToday, "Ngày khám", appointmentDate)
//            DetailRow(Icons.Outlined.AccessTime, "Giờ khám", appointmentTime)
//            DetailRow(Icons.Outlined.Person, "Bệnh nhân", patientName)
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        DetailSection("Thanh toán") {
//            DetailRow(Icons.Outlined.Payments, "Phí khám", formatPrice(fee))
//            DetailRow(Icons.Outlined.ReceiptLong, "Mã phiếu", bookingId)
//        }
//
//        if (showQr) {
//            Spacer(Modifier.height(24.dp))
//
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(20.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(24.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(90.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                            .background(colorScheme.surface),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(
//                            Icons.Default.QrCode,
//                            null,
//                            modifier = Modifier.size(50.dp)
//                        )
//                    }
//
//                    Spacer(Modifier.height(12.dp))
//
//                    Text("Đưa mã này cho lễ tân khi đến khám")
//                }
//            }
//        }
//    }
//}