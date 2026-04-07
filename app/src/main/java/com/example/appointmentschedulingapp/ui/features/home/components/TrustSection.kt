package com.example.appointmentschedulingapp.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appointmentschedulingapp.domain.model.Clinic

@Composable
fun TrustSectionNew(
    clinics: List<Clinic>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ĐƯỢC TIN TƯỞNG HỢP TÁC VÀ ĐỒNG HÀNH",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            ),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            clinics.forEach { clinic ->
                PartnerItem(clinic)
            }
        }
    }
}

@Composable
fun PartnerItem(clinic: Clinic) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp) // fix width để item đều nhau
    ) {
        AsyncImage(
            model = clinic.imageUrl,
            contentDescription = clinic.name,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(26.dp))
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = clinic.name,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            maxLines = 2,
            lineHeight = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}