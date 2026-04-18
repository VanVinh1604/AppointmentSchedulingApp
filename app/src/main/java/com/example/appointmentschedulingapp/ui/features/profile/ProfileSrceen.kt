package com.example.appointmentschedulingapp.ui.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.appointmentschedulingapp.domain.model.PatientProfile
import com.example.appointmentschedulingapp.ui.components.login.AppScreen
import com.example.appointmentschedulingapp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    isLoggedIn: Boolean,
    navController: NavHostController,
    onNavigateToCreateProfile: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    AppScreen(
        requiresLogin = true,
        isLoggedIn = isLoggedIn,
        onNavigateToLogin = { navController.navigate(Screen.Auth.route) }
    ) {
        val viewModel: ProfileViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()

        val primaryColor = Color(0xFF1976D2)
        val lightBlueBg = Color(0xFFE3F2FD).copy(alpha = 0.8f)
        val darkBlueText = Color(0xFF1565C0)

        // Reload khi quay lại từ CreateProfile
        LaunchedEffect(Unit) {
            viewModel.loadProfiles()
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Hồ sơ bệnh nhân",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* Quay lại */ }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToCreateProfile) {
                            Icon(
                                Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = primaryColor)
                )
            }
        ) { paddingValues ->
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                }

                uiState.profiles.isNotEmpty() -> {
                    // --- CÓ HỒ SƠ: Hiển thị danh sách ---
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(Color(0xFFF5F5F5)),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.profiles) { profile ->
                            PatientProfileItem(
                                profile = profile,
                                primaryColor = primaryColor,
                                darkBlueText = darkBlueText,
                                lightBlueBg = lightBlueBg,
                                onClick = { onNavigateToDetail(profile.id) }  // ← thêm

                            )
                        }
                    }
                }

                else -> {
                    // --- KHÔNG CÓ HỒ SƠ: Hiển thị màn hình trống như cũ ---
                    EmptyProfileContent(
                        paddingValues = paddingValues,
                        primaryColor = primaryColor,
                        lightBlueBg = lightBlueBg,
                        darkBlueText = darkBlueText,
                        onCreateNew = onNavigateToCreateProfile
                    )
                }
            }
        }
    }
}

// --- ITEM HỒ SƠ ---
@Composable
private fun PatientProfileItem(
    profile: PatientProfile,
    primaryColor: Color,
    onClick: () -> Unit,
    darkBlueText: Color,
    lightBlueBg: Color,

) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar icon
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = lightBlueBg
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.fullName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    if (profile.isDefault) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = primaryColor
                        ) {
                            Text(
                                text = "Mặc định",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Ngày sinh: ${profile.dateOfBirth}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                if (profile.phoneNumber.isNotBlank()) {
                    Text(
                        text = "SĐT: ${profile.phoneNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                if (!profile.relationship.isNullOrBlank()) {
                    Text(
                        text = "Quan hệ: ${profile.relationship}",
                        style = MaterialTheme.typography.bodySmall,
                        color = darkBlueText
                    )
                }
            }
        }
    }
}

// --- MÀN HÌNH TRỐNG (giữ nguyên UI cũ) ---
@Composable
private fun EmptyProfileContent(
    paddingValues: PaddingValues,
    primaryColor: Color,
    lightBlueBg: Color,
    darkBlueText: Color,
    onCreateNew: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = lightBlueBg),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = CircleShape,
                    color = primaryColor
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Bạn chưa có hồ sơ bệnh nhân. Vui lòng tạo mới hồ sơ để được đặt khám.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = darkBlueText
                    ),
                    maxLines = 2,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tạo hồ sơ bệnh nhân",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Bạn được phép tạo tối đa 10 hồ sơ\n(cá nhân và người thân trong gia đình)",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            color = primaryColor,
            onClick = onCreateNew
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    "CHƯA TỪNG KHÁM - ĐĂNG KÝ MỚI",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp)
                .border(1.dp, primaryColor, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            onClick = { /* Logic quét mã */ }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = primaryColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text("QUÉT MÃ BHYT/CCCD", color = primaryColor, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}