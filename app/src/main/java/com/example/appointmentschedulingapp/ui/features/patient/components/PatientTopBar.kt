package com.example.appointmentschedulingapp.ui.features.patient.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.appointmentschedulingapp.ui.features.patient.PatientUiColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientTopBar(
    onBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Tạo hồ sơ bệnh nhân",
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = PatientUiColors.PrimaryBlue,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}