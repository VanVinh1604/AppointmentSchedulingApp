package com.example.appointmentschedulingapp.ui.features.patient.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appointmentschedulingapp.ui.features.patient.PatientUiColors

@Composable
fun BottomSaveBar(
    isDefault: Boolean,
    isLoading: Boolean,
    onToggle: (Boolean) -> Unit,
    isFormValid: Boolean,
    onSave: () -> Unit
) {
    Surface(shadowElevation = 8.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    onToggle(!isDefault)
                }
            ) {
                Checkbox(
                    checked = isDefault,
                    onCheckedChange = onToggle
                )
                Text("Đặt làm hồ sơ mặc định")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = PatientUiColors.PrimaryBlue,
                    disabledContainerColor = androidx.compose.ui.graphics.Color.LightGray
                ),
                onClick = onSave,
                enabled = !isLoading && isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Lưu hồ sơ")
                }
            }
        }
    }
}