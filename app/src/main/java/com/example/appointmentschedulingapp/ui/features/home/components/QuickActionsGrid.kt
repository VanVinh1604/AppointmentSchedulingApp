package com.example.appointmentschedulingapp.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointmentschedulingapp.domain.model.HomeAction

@Composable
fun QuickActionsGrid(
    actions: List<HomeAction>,
    onActionClick: (HomeAction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            actions.chunked(4).forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { action ->
                        ActionItem(
                            action = action,
                            modifier = Modifier.weight(1f),
                            onClick = { onActionClick(action) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

fun getIconFromName(iconName: String): ImageVector {
    return when (iconName) {
        "clinic" -> Icons.Default.AddBusiness
        "specialty" -> Icons.Default.PersonSearch
        "science" -> Icons.Default.Science
        "video" -> Icons.Default.VideoCall
        "pharmacy" -> Icons.Default.LocalPharmacy
        "health_record" -> Icons.Default.FolderShared
        "result" -> Icons.Default.Description
        "reminder" -> Icons.Default.Alarm
        "chat" -> Icons.Default.Chat
        "enterprise" -> Icons.Default.Apartment
        else -> Icons.Default.Home
    }
}
@Composable
fun ActionItem(
    action: HomeAction,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getIconFromName(action.iconName),
                contentDescription = null,
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = action.title,
            modifier = Modifier.padding(top = 8.dp, start = 4.dp, end = 4.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                lineHeight = 13.sp
            ),
            maxLines = 2
        )
    }
}

