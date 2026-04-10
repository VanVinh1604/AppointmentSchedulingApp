package com.example.appointmentschedulingapp.ui.features.patient.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DocumentSection(
    identityCard: String,
    onIdentityCardChange: (String) -> Unit,
    insuranceNumber: String,
    onInsuranceChange: (String) -> Unit,
    insuranceExpiry: String,
    onExpiryChange: (String) -> Unit
) {
    SectionCard(title = "Giấy tờ & bảo hiểm") {
        ProfileTextField(
            label = "Số CCCD",
            value = identityCard,
            onChange = onIdentityCardChange
        )

        Spacer(Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProfileTextField(
                label = "Mã BHYT",
                value = insuranceNumber,
                onChange = onInsuranceChange,
                modifier = Modifier.weight(1.6f)
            )

            ProfileTextField(
                label = "Hạn BHYT",
                value = insuranceExpiry,
                onChange = onExpiryChange,
                placeholder = "MM/yyyy",
                modifier = Modifier.weight(1f)
            )
        }
    }
}