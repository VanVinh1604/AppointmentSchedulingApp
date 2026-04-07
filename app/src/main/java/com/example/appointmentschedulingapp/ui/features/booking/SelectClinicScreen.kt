package com.example.appointmentschedulingapp.ui.features.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.ui.features.booking.components.BookingTypeSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectClinicScreen( onBack: () -> Unit,
                        onNavigateToDetail: (String) -> Unit,
                        onNavigateToBookingStep1: () -> Unit,
                        viewModel: ClinicViewModel = hiltViewModel(),
                        bookingViewModel: BookingViewModel = hiltViewModel()) {

    val clinics by viewModel.clinics.collectAsState()
    val primaryColor = Color(0xFF1976D2)
    val lightBlue = Color(0xFFE3F2FD)

//    val bookingViewModel: BookingViewModel = hiltViewModel()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedClinic by remember { mutableStateOf<Clinic?>(null) }

    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chọn cơ sở y tế", color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = primaryColor
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {

            // --- SEARCH & FILTER ROW ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Tìm kiếm cơ sở...", fontSize = 14.sp) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    // Cập nhật tham số colors chuẩn M3
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = null, tint = primaryColor)
                    Text("Lọc", fontSize = 10.sp, color = primaryColor, fontWeight = FontWeight.Bold)
                }
            }

            // --- FILTER CHIPS ---
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                listOf("Tất cả", "Bệnh viện", "Phòng mạch").forEach { label ->
                    val isSelected = label == "Tất cả"
                    Surface(
                        modifier = Modifier.padding(end = 8.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) primaryColor else Color.White,
                        border = if (!isSelected) BorderStroke(1.dp, Color.LightGray) else null
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            color = if (isSelected) Color.White else Color.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // --- CLINIC LIST ---
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(clinics, key = { it.id }) { clinic ->
                    ClinicCard(
                        clinic = clinic,
                        primaryColor = primaryColor,
                        lightBlue = lightBlue,
                        onDetailClick = onNavigateToDetail,
                        onBookingClick = {
                            selectedClinic = clinic
                            showBottomSheet = true
                        }
                    )
                }
            }

            // --- MODAL BOTTOM SHEET ---
            if (showBottomSheet && selectedClinic != null) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    BookingTypeSheetContent(
                        clinicName = selectedClinic!!.name,
                        onClose = { showBottomSheet = false },
                        onTypeSelected = { serviceType ->
                            showBottomSheet = false

                            // 2. LƯU DỮ LIỆU VÀO VIEWMODEL TRƯỚC KHI CHUYỂN TRANG
                            bookingViewModel.onEvent(
                                BookingEvent.SelectClinic(selectedClinic!!)
                            )

                            bookingViewModel.onEvent(
                                BookingEvent.SelectService(serviceType)
                            )

                            onNavigateToBookingStep1() // Sau đó mới chuyển trang
                        }
                    )
                }
            }

        }

    }
}

@Composable
fun ClinicCard( clinic: Clinic,
                primaryColor: Color, lightBlue: Color
                , onDetailClick: (String) -> Unit
                , onBookingClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo placeholder
                AsyncImage(
                    model = clinic.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(65.dp) // Tăng kích thước nhẹ cho dễ nhìn
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF0F0F0)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        clinic.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Text(
                        clinic.address,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Text(
                        " (${clinic.rating})",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        repeat(5) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                        }
                        Text(" (4.8)", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }

            // Bottom 25% area with background
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightBlue.copy(alpha = 0.5f))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {onDetailClick(clinic.id)},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, primaryColor)
                ) {
                    Text("Xem chi tiết", color = primaryColor, fontSize = 13.sp)
                }
                Button(
                    onClick = onBookingClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("Đặt khám ngay", color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}