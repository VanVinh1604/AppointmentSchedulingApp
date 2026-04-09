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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState
import com.example.appointmentschedulingapp.ui.features.booking.components.BookingFlowTopBar
import com.example.appointmentschedulingapp.ui.features.booking.components.EmptyPatientState

@Composable
fun BookingStep2Screen(
    uiState: BookingUiState,
    patientList: List<String>,
    onPatientSelected: (String, String) -> Unit,
    onCreatePatient: () -> Unit,
    onBack: () -> Unit
) {
    val primaryBlue = Color(0xFF1976D2)

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

        if (patientList.isEmpty()) {
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

                items(patientList) { name ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                onPatientSelected("ID_$name", name)
                            },
                        border = if (uiState.patientName == name)
                            BorderStroke(2.dp, primaryBlue)
                        else null
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(
                                Icons.Default.Person,
                                null,
                                tint = primaryBlue
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(name)
                        }
                    }
                }
            }
        }
    }
}