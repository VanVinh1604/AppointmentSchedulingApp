package com.example.appointmentschedulingapp.ui.features.booking

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicDetailScreen(
    clinicId: String,
    onBack: () -> Unit,
    viewModel: ClinicDetailViewModel = hiltViewModel()
) {
    val clinic by viewModel.clinic.collectAsState()

    LaunchedEffect(clinicId) {
        viewModel.loadClinic(clinicId)
    }

    val primaryColor = Color(0xFF1976D2)
    var selectedTab by remember { mutableStateOf("Giới thiệu") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.FavoriteBorder, null, tint = Color.Gray)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Share, null, tint = Color.Gray)
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text("ĐẶT KHÁM NGAY", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->

        if (clinic == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val clinicData = clinic!!

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // 1. ẢNH BÌA (BANNER) - Thay thế cho Box màu xám
                AsyncImage(
                    model = clinicData.imageUrl,
                    contentDescription = "Clinic Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop // Cắt ảnh cho vừa khung
                )

                // 2. LOGO TRÒN - Thay thế cho Surface chứa Icon Business
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp)
                        .size(80.dp),
                    shape = CircleShape,
                    border = BorderStroke(3.dp, Color.White),
                    shadowElevation = 4.dp
                ) {
                    AsyncImage(
                        model = clinicData.imageUrl, // Dùng cùng link ảnh hoặc link logo riêng
                        contentDescription = "Clinic Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                // NAME
                Text(
                    text = clinicData.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                // RATING
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    repeat(5) {
                        Icon(
                            Icons.Default.Star,
                            null,
                            Modifier.size(16.dp),
                            Color(0xFFFFB300)
                        )
                    }

                    Text(
                        text = " ${clinicData.rating}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " (${clinicData.reviewsCount} đánh giá)",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // ADDRESS
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        Modifier.size(16.dp),
                        primaryColor
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = clinicData.address,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }

                // SERVICES
                Spacer(Modifier.height(24.dp))
                Text("Các dịch vụ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))

                val services = listOf(
                    ServiceItemData("Khám dịch vụ", Icons.Default.MedicalServices),
                    ServiceItemData("Khám theo bác sĩ", Icons.Default.Person),
                    ServiceItemData("Khám ngoài giờ", Icons.Default.HistoryToggleOff),
                    ServiceItemData("Khám VIP", Icons.Default.Stars),
                    ServiceItemData("Khám thường", Icons.Default.HealthAndSafety),
                    ServiceItemData("Tái khám", Icons.Default.Bed)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(services) {
                        ServiceCard(it, primaryColor)
                    }
                }

                // TABS
                Spacer(Modifier.height(24.dp))
                Text("Mô tả", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                val tabs = listOf(
                    "Giới thiệu",
                    "Chuyên khoa",
                    "Bảng giá",
                    "Hướng dẫn",
                    "Câu hỏi",
                    "Vị trí",
                    "Đánh giá"
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(tabs) { tab ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { selectedTab = tab }
                        ) {
                            Text(
                                text = tab,
                                color = if (selectedTab == tab) primaryColor else Color.Gray,
                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp
                            )

                            if (selectedTab == tab) {
                                Box(
                                    Modifier
                                        .height(2.dp)
                                        .width(40.dp)
                                        .background(primaryColor)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                when (selectedTab) {
                    "Giới thiệu" -> {
                        Text(
                            text = clinicData.description.ifBlank { "Chưa có mô tả cho cơ sở y tế này." },
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = Color.DarkGray
                        )
                    }
                    "Chuyên khoa" -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (clinicData.specialties.isEmpty()) {
                                Text("Đang cập nhật danh sách chuyên khoa...", color = Color.Gray)
                            } else {
                                clinicData.specialties.forEach { specialty ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, null, Modifier.size(16.dp), primaryColor)
                                        Spacer(Modifier.width(8.dp))
                                        Text(text = specialty, fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                    "Bảng giá" -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Phí khám tư vấn", fontWeight = FontWeight.Medium)
                                Text(
                                    text = "${clinicData.consultationFee}đ",
                                    color = primaryColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    "Vị trí" -> {
                        Column {
                            Text("Địa chỉ cụ thể:", fontWeight = FontWeight.Bold)
                            Text("${clinicData.address}, ${clinicData.district}, ${clinicData.city}")
                            Spacer(Modifier.height(8.dp))
                            // Ở đây sau này bạn có thể tích hợp Google Maps bằng latitude/longitude
                            Text("Tọa độ: ${clinicData.latitude}, ${clinicData.longitude}", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                    "Đánh giá" -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "${clinicData.rating}", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text("trên 5 sao")
                                Text("${clinicData.reviewsCount} người đã đánh giá", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                    else -> {
                        // Các tab chưa có dữ liệu cụ thể (Hướng dẫn, Câu hỏi, Đánh giá)
                        Text("Nội dung $selectedTab đang được cập nhật...", color = Color.Gray)
                    }
                }

                Spacer(Modifier.height(100.dp))
            }
        }
    }
}

data class ServiceItemData(val title: String, val icon: ImageVector)

@Composable
fun ServiceCard(data: ServiceItemData, primaryColor: Color) {
    Card(
        modifier = Modifier
            .size(width = 140.dp, height = 110.dp), // Kích thước vừa vặn
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Căn giữa theo chiều ngang
            verticalArrangement = Arrangement.Center // Căn giữa theo chiều dọc
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(primaryColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = data.icon,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center, // Căn giữa nội dung chữ
                lineHeight = 16.sp,
                maxLines = 2
            )
        }
    }
}