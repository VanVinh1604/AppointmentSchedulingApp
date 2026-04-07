package com.example.appointmentschedulingapp.ui.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appointmentschedulingapp.domain.model.User
// Import các component đã tách
import com.example.appointmentschedulingapp.ui.features.home.components.*


@Composable
fun HomeScreen(onNavigate: (String) -> Unit,
               session: User,
               viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is HomeEvent.Navigate -> {
                    try {
                        onNavigate(event.route)
                    } catch (e: Exception) {
                        android.util.Log.e("NavError", "Route không tồn tại: ${event.route}")
                    }
                }
            }
        }
    }
    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 🔥 Phần cố định (Header & Search)
            HomeHeader(
                username = if (session.isLoggedIn) session.phoneNumber else "Khách"
            )
            SearchBarSection()

            // 🔥 Phần nội dung cuộn (LazyColumn)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    QuickActionsGrid(
                        actions = uiState.actions,
                        onActionClick = viewModel::onActionClicked
                    )
                }
                item { TrustSectionNew() }
// Trong HomeScreen.kt
                item{HospitalSection(
                    onViewAllClick = {
                        onNavigate("select_clinic") // Dùng callback onNavigate thay vì navController
                    },
                    onClinicClick = { id ->
                        onNavigate("clinic_detail/$id") // Dùng callback onNavigate
                    },
                    onBookingClick = { clinic ->
                        // Xử lý booking hoặc chuyển trang
                        onNavigate("booking_step_1")
                    }
                )}
                item { PromotionBanner() }
                item { DoctorSection() }
                item { CareSection() }
                // Khoảng trống để không bị chạm Bottom Nav
                item { Spacer(modifier = Modifier.height(5.dp)) }
            }

        }
    }
}