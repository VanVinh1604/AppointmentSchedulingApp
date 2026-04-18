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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appointmentschedulingapp.ui.components.login.AppScreen
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState
import com.example.appointmentschedulingapp.ui.features.booking.components.BookingFlowTopBar
import com.example.appointmentschedulingapp.ui.features.booking.components.EmptyPatientState
import com.example.appointmentschedulingapp.ui.navigation.Screen

@Composable
fun BookingStep2Screen(
    isLoggedIn: Boolean,
    navController: NavHostController,
    uiState: BookingUiState,
    onLoadPatients: () -> Unit,
    onPatientSelected: (String, String) -> Unit,
    onCreatePatient: () -> Unit,
    onBack: () -> Unit
) {
    // MỞ NGOẶC NHỌN Ở ĐÂY
    AppScreen(
        requiresLogin = true,
        isLoggedIn = isLoggedIn,
        onNavigateToLogin = { navController.navigate(Screen.Auth.route) }
    ) {
        val primaryBlue = Color(0xFF1976D2)

        LaunchedEffect(Unit) {
            onLoadPatients()
        }

        Scaffold(
            topBar = {
                BookingFlowTopBar(
                    title = "Chọn hồ sơ bệnh nhân",
                    currentStep = 2,
                    onBack = onBack
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onCreatePatient,
                    containerColor = primaryBlue
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Tạo hồ sơ", color = Color.White)
                }
            }
        ) { padding ->
            if (uiState.patientProfiles.isEmpty()) {
                EmptyPatientState(
                    modifier = Modifier.padding(padding),
                    onCreatePatient = onCreatePatient
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Text(
                            "Chọn hồ sơ để tiếp tục",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    items(uiState.patientProfiles) { patient ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onPatientSelected(patient.id, patient.fullName)
                                },
                            border = if (uiState.selectedPatientId == patient.id)
                                BorderStroke(2.dp, primaryBlue)
                            else null
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                Icon(Icons.Default.Person, null, tint = primaryBlue)
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = patient.fullName,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = patient.phoneNumber,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}