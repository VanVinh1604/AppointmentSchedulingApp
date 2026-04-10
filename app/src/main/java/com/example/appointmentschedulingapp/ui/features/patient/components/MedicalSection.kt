package com.example.appointmentschedulingapp.ui.features.patient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.appointmentschedulingapp.ui.features.patient.PatientUiColors

@Composable
fun MedicalSection(
    allergies: String,
    onAllergiesChange: (String) -> Unit,
    medicalHistory: String,
    onHistoryChange: (String) -> Unit
) {
    SectionCard(title = "Thông tin y tế") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    PatientUiColors.WarnBg,
                    RoundedCornerShape(10.dp)
                )
                .border(
                    1.dp,
                    PatientUiColors.WarnBorder,
                    RoundedCornerShape(10.dp)
                )
                .padding(12.dp)
        ) {
            Row {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = PatientUiColors.WarnText
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    "Dị ứng",
                    color = PatientUiColors.WarnText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(6.dp))

            ProfileTextField(
                label = "Thông tin dị ứng",
                value = allergies,
                onChange = onAllergiesChange
            )
        }

        Spacer(Modifier.height(10.dp))

        ProfileMultilineField(
            label = "Tiền sử bệnh",
            value = medicalHistory,
            onChange = onHistoryChange
        )
    }
}