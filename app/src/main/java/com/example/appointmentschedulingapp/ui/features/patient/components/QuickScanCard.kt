package com.example.appointmentschedulingapp.ui.features.patient.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.appointmentschedulingapp.ui.features.patient.PatientUiColors

@Composable
fun QuickScanCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = PatientUiColors.LightBlue
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                "NHẬP NHANH QUA GIẤY TỜ",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = PatientUiColors.PrimaryBlue
            )

            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ScanButton(
                    label = "QR CCCD",
                    icon = Icons.Default.QrCodeScanner,
                    modifier = Modifier.weight(1f)
                )

                ScanButton(
                    label = "Scan BHYT",
                    icon = Icons.Default.DocumentScanner,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ScanButton(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = {},
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = PatientUiColors.PrimaryBlue
        ),
        border = BorderStroke(1.dp, PatientUiColors.BlueBorder)
    ) {
        Icon(icon, null)
        Spacer(Modifier.width(6.dp))
        Text(label)
    }
}