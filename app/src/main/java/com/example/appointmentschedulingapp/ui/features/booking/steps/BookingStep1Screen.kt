package com.example.appointmentschedulingapp.ui.features.booking.steps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appointmentschedulingapp.ui.features.booking.BookingEvent
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState
import com.example.appointmentschedulingapp.ui.features.booking.BookingViewModel
import com.example.appointmentschedulingapp.ui.features.booking.components.BookingFlowTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingStep1Screen(
    onBack: () -> Unit,
    onEvent: (BookingEvent) -> Unit,
    uiState: BookingUiState,
    onNext: () -> Unit,
) {
    val viewModel: BookingViewModel = hiltViewModel()

    val primaryColor = Color(0xFF1976D2)
    var showSpecialtySheet by remember { mutableStateOf(false) }
    var showServiceSheet by remember { mutableStateOf(false) }
    var showTimeSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }


    val isFormValid =
        uiState.selectedSpecialty.isNotBlank() &&
                uiState.selectedBookingType.isNotBlank() &&
                uiState.selectedDate.isNotBlank() &&
                uiState.selectedTime.isNotBlank()
    Scaffold(
        topBar = {
            BookingFlowTopBar(
                title = "Chọn thông tin khám",
                currentStep = 1,
                onBack = onBack
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = onNext,
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text(
                        "TIẾP TỤC",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

        }
    ) { padding ->
        // BookingStep1Screen.kt
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // 1. Card Thông tin bệnh viện
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            uiState.selectedClinic?.name ?: "Bệnh viện chưa xác định",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.selectedClinic?.address ?: "Địa chỉ chưa cập nhật",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Danh sách lựa chọn (Chuyên khoa, Dịch vụ, Ngày, Giờ)
                SelectionField(
                    label = "Chuyên khoa",
                    value = uiState.selectedSpecialty.ifEmpty { "Chọn chuyên khoa" },
                    icon = Icons.Default.MedicalServices
                ) {
                    showSpecialtySheet = true
                }

                SelectionField(
                    label = "Hình thức khám",
                    value = uiState.selectedBookingType.ifEmpty { "Chọn hình thức khám" },
                    icon = Icons.Default.MedicalServices,
                    onClick = {
                        showServiceSheet = true
                    }
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F9FF)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFF1976D2)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Số phòng khám sẽ được bệnh viện thông báo khi bạn đến quầy lễ tân.",
                            fontSize = 13.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }

                SelectionField(
                    label = "Ngày khám",
                    value = uiState.selectedDate.ifEmpty { "Chọn ngày khám" },
                    icon = Icons.Default.CalendarMonth
                ) {
                    showDatePicker = true
                }

                SelectionField(
                    label = "Giờ khám",
                    value = uiState.selectedTime.ifEmpty { "Chọn buổi khám" },
                    icon = Icons.Default.AccessTime
                ) {
                    showTimeSheet = true
                }
            }
            uiState.step1Error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (showSpecialtySheet) {
                ModalBottomSheet(onDismissRequest = { showSpecialtySheet = false }) {
                    uiState.selectedClinic?.specialties?.forEach { specialty ->
                        ListItem(
                            headlineContent = { Text(specialty) },
                            modifier = Modifier.clickable {
                                onEvent(BookingEvent.UpdateSpecialty(specialty))
                                showSpecialtySheet = false
                            }
                        )
                    }
                }
            }

            if (showServiceSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showServiceSheet = false
                    }
                ) {
                    uiState.selectedClinic?.services?.forEach { service ->
                        ListItem(
                            headlineContent = { Text(service) },
                            modifier = Modifier.clickable {
                                onEvent(BookingEvent.SelectService(service))
                                showServiceSheet = false
                            }
                        )
                    }
                }
            }

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val selectedMillis = datePickerState.selectedDateMillis
                                if (selectedMillis != null) {
                                    val formattedDate = java.text.SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        java.util.Locale.getDefault()
                                    ).format(java.util.Date(selectedMillis))

                                    onEvent(BookingEvent.SelectDate(formattedDate))
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("Xác nhận")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDatePicker = false }
                        ) {
                            Text("Hủy")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
            if (showTimeSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showTimeSheet = false }
                ) {
                    listOf(
                        "Buổi sáng (07:00 - 11:00)",
                        "Buổi chiều (13:00 - 17:00)"
                    ).forEach { time ->
                        ListItem(
                            headlineContent = { Text(time) },
                            modifier = Modifier.clickable {
                                onEvent(BookingEvent.SelectTime(time))
                                showTimeSheet = false
                            }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun BookingStepper(currentStep: Int) {
    val steps = listOf(
        Icons.Default.Info,           // Bước 1: Thông tin
        Icons.Default.Person,         // Bước 2: Chọn hồ sơ
        Icons.Default.Payment,        // Bước 3: Thanh toán
        Icons.Default.CheckCircle     // Bước 4: Hoàn tất
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, icon ->
            val stepNum = index + 1
            val isActive = stepNum == currentStep

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(if (isActive) 36.dp else 30.dp) // Bước hiện tại to hơn
                        .background(
                            if (isActive) Color.White else Color.White.copy(alpha = 0.3f),
                            CircleShape
                        )
                        .border(
                            if (isActive) 2.dp else 0.dp,
                            if (isActive) Color.White else Color.Transparent,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(if (isActive) 20.dp else 16.dp), // Icon sáng và to hơn
                        tint = if (isActive) Color(0xFF1976D2) else Color.White
                    )
                }
            }

            // Vẽ đường nối giữa các bước (trừ bước cuối)
            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.5f))
                )
            }
        }
    }
}

@Composable
fun SelectionField(label: String, value: String, icon: ImageVector, onClick: () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedCard(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, null, tint = Color(0xFF1976D2), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    value,
                    color = if (value.contains("Chọn")) Color.Gray else Color.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
            }
        }
    }
}