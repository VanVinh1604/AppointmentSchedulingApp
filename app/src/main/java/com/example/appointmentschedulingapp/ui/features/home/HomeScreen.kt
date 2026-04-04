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
// Import các component đã tách
import com.example.appointmentschedulingapp.ui.features.home.components.*


@Composable
fun HomeScreen(onNavigate: (String) -> Unit,
               viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect{ event ->
            when(event){
                is HomeEvent.Navigate -> onNavigate(event.route)
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
            HomeHeader(username = "Nguyễn Văn A")
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
                item { HospitalSection() }
                item { PromotionBanner() }
                item { DoctorSection() }
                item { CareSection() }
                // Khoảng trống để không bị chạm Bottom Nav
                item { Spacer(modifier = Modifier.height(5.dp)) }
            }
        }
    }
}