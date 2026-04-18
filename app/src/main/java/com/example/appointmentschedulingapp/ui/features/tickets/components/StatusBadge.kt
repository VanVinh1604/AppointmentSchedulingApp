package com.example.appointmentschedulingapp.ui.features.tickets.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus

@Composable
fun StatusBadge(statusUi: StatusUi) {
    val colorScheme = MaterialTheme.colorScheme

    // Map BookingStatus visually
    val (containerColor, contentColor) = when (statusUi.label) {
        BookingStatus.CONFIRMED.label -> colorScheme.primaryContainer to colorScheme.primary
        BookingStatus.FAILED.label    -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.error
        BookingStatus.PAID.label      -> colorScheme.tertiaryContainer to colorScheme.tertiary
        BookingStatus.COMPLETED.label -> colorScheme.secondaryContainer to colorScheme.secondary
        BookingStatus.CANCELLED.label -> colorScheme.surfaceVariant to colorScheme.outline
        else                          -> colorScheme.surfaceVariant to colorScheme.onSurfaceVariant
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                statusUi.icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(11.dp)
            )
            Spacer(Modifier.width(3.dp))
            Text(
                statusUi.label,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}