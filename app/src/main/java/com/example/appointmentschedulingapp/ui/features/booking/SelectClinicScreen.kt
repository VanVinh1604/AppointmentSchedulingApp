package com.example.appointmentschedulingapp.ui.features.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun SelectClinicScreen(
    onBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToBookingStep1: () -> Unit,
    viewModel: ClinicViewModel = hiltViewModel(),
    bookingViewModel: BookingViewModel = hiltViewModel()
) {
    val primaryColor = Color(0xFF1976D2)
    val lightBlue = Color(0xFFE3F2FD)

    val filteredClinics by viewModel.filteredClinics.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val availableCities by viewModel.availableCities.collectAsState()

    var showCityFilterSheet by remember { mutableStateOf(false) }
    var citySearchQuery by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedClinic by remember { mutableStateOf<Clinic?>(null) }

    val bookingSheetState = rememberModalBottomSheetState()
    val filterSheetState = rememberModalBottomSheetState()

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    placeholder = { Text("Tìm kiếm cơ sở...", fontSize = 14.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))

                // --- NÚT LỌC MỞ BOTTOM SHEET ---
                Surface(
                    onClick = {
                        citySearchQuery = ""
                        showCityFilterSheet = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedCity != "Tất cả") primaryColor else Color.White,
                    border = BorderStroke(1.dp, if (selectedCity != "Tất cả") primaryColor else Color.LightGray),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Lọc",
                            tint = if (selectedCity != "Tất cả") Color.White else primaryColor
                        )
                    }
                }
            }

            // --- CHIP HIỂN THỊ THÀNH PHỐ ĐANG CHỌN (nếu có) ---
            if (selectedCity != "Tất cả") {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = lightBlue,
                        border = BorderStroke(1.dp, primaryColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = primaryColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(selectedCity, fontSize = 12.sp, color = primaryColor, fontWeight = FontWeight.Medium)
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Xóa lọc",
                                tint = primaryColor,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { viewModel.onCitySelected("Tất cả") }
                            )
                        }
                    }
                }
            }

            // --- CLINIC LIST ---
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredClinics, key = { it.id }) { clinic ->
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
        }

        // --- BOTTOM SHEET LỌC THÀNH PHỐ ---
        if (showCityFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCityFilterSheet = false },
                sheetState = filterSheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 32.dp)
                ) {
                    // Tiêu đề
                    Text(
                        "Chọn tỉnh / thành phố",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Thanh tìm kiếm tỉnh thành
                    OutlinedTextField(
                        value = citySearchQuery,
                        onValueChange = { citySearchQuery = it },
                        placeholder = { Text("Tìm tỉnh thành...", fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                        },
                        trailingIcon = {
                            if (citySearchQuery.isNotEmpty()) {
                                IconButton(onClick = { citySearchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                                }
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color(0xFFF8F8F8),
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Danh sách tỉnh thành được lọc theo citySearchQuery
                    val filteredCities = availableCities.filter {
                        it.contains(citySearchQuery, ignoreCase = true)
                    }

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(filteredCities) { city ->
                            val isSelected = city == selectedCity
                            Surface(
                                onClick = {
                                    viewModel.onCitySelected(city)
                                    showCityFilterSheet = false
                                },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) lightBlue else Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            if (city == "Tất cả") Icons.Default.Public else Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = if (isSelected) primaryColor else Color.Gray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            city,
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                            color = if (isSelected) primaryColor else Color(0xFF1A1A1A)
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = primaryColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- BOTTOM SHEET ĐẶT KHÁM ---
        if (showBottomSheet && selectedClinic != null) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bookingSheetState
            ) {
                BookingTypeSheetContent(
                    clinicName = selectedClinic!!.name,
                    onClose = { showBottomSheet = false },
                    onTypeSelected = { serviceType ->
                        showBottomSheet = false
                        bookingViewModel.onEvent(BookingEvent.SelectClinic(selectedClinic!!))
                        bookingViewModel.onEvent(BookingEvent.SelectService(serviceType))
                        onNavigateToBookingStep1()
                    }
                )
            }
        }
    }
}

@Composable
fun ClinicCard(
    clinic: Clinic,
    primaryColor: Color,
    lightBlue: Color,
    onDetailClick: (String) -> Unit,
    onBookingClick: () -> Unit
) {
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
                AsyncImage(
                    model = clinic.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF0F0F0)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(clinic.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(clinic.address, fontSize = 12.sp, color = Color.Gray)
                    Text(" (${clinic.rating})", fontSize = 11.sp, color = Color.Gray)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        repeat(5) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(" (4.8)", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightBlue.copy(alpha = 0.5f))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onDetailClick(clinic.id) },
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