package com.example.appointmentschedulingapp.ui.features.patient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import com.example.appointmentschedulingapp.ui.features.patient.PatientUiColors

@Composable
fun RelationshipSection(
    relationship: String,
    onRelationshipChange: (String) -> Unit,
    emergencyContact: String,
    onEmergencyChange: (String) -> Unit
) {
    val options = listOf("Bản thân", "Con", "Cha", "Mẹ", "Vợ/Chồng", "Khác")

    SectionCard(title = "Quan hệ & liên lạc") {
        Text(
            "Quan hệ với chủ tài khoản",
            fontSize = 12.sp,
            color = PatientUiColors.SectionLabel
        )

        Spacer(Modifier.height(8.dp))

        options.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { option ->
                    val selected = option == relationship

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(
                                if (selected) PatientUiColors.PrimaryBlue
                                else Color.Transparent,
                                RoundedCornerShape(20.dp)
                            )
                            .border(
                                1.dp,
                                if (selected) PatientUiColors.PrimaryBlue
                                else PatientUiColors.FieldBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { onRelationshipChange(option) }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            option,
                            fontSize = 12.sp,
                            color = if (selected) Color.White
                            else PatientUiColors.SectionLabel
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))
        }

        Spacer(Modifier.height(4.dp))

        ProfileTextField(
            label = "Liên hệ khẩn cấp",
            value = emergencyContact,
            onChange = onEmergencyChange
        )
    }
}