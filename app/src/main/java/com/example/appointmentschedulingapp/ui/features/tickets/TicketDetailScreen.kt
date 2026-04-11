package com.example.appointmentschedulingapp.ui.features.tickets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.ui.features.tickets.components.StatusBadge
import com.example.appointmentschedulingapp.ui.features.tickets.components.StatusUi
import com.example.appointmentschedulingapp.common.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    booking: Booking,
    onBack: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val statusUi = booking.status.toUi()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Chi tiết phiếu khám",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorScheme.primary
                )
            )
        }
    ) { padding ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 3 }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ===== Status Header Card =====
                StatusHeaderCard(booking = booking, statusUi = statusUi)

                // ===== Appointment Info =====
                AppointmentDetailCard(booking = booking)

                // ===== Clinic Info =====
                ClinicDetailCard(booking = booking)

                // ===== Doctor Info =====
                if (booking.doctorName.isNotEmpty()) {
                    DoctorDetailCard(booking = booking)
                }

                // ===== Patient Info =====
                PatientDetailCard(booking = booking)

                // ===== Payment Info =====
                PaymentDetailCard(booking = booking)

                // ===== QR Code =====
                QrCodeCard(booking = booking)

                // ===== Important Notes =====
                ImportantNotesCard()

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// ===== STATUS HEADER CARD =====

@Composable
private fun StatusHeaderCard(booking: Booking, statusUi: StatusUi) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        booking.clinicName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        booking.specialty.ifEmpty { "—" },
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurface.copy(alpha = 0.65f)
                    )
                }
                StatusBadge(statusUi)
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HighlightChip(
                    icon = Icons.Outlined.CalendarToday,
                    label = "Ngày khám",
                    value = booking.appointmentDate.ifEmpty { "—" },
                    modifier = Modifier.weight(1f)
                )
                HighlightChip(
                    icon = Icons.Outlined.Schedule,
                    label = "Giờ khám",
                    value = booking.appointmentTime.ifEmpty { "—" },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// ===== APPOINTMENT DETAIL CARD =====

@Composable
private fun AppointmentDetailCard(booking: Booking) {
    val colorScheme = MaterialTheme.colorScheme

    ReceiptCard(title = "Thông tin lịch hẹn", icon = Icons.Outlined.CalendarMonth) {
        DetailRow(
            icon = Icons.Outlined.MedicalServices,
            label = "Chuyên khoa",
            value = booking.specialty.ifEmpty { "—" }
        )
        if (booking.bookingType.isNotEmpty()) {
            HorizontalDivider(
                color = colorScheme.outlineVariant.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            DetailRow(
                icon = Icons.Outlined.Healing,
                label = "Hình thức",
                value = booking.bookingType
            )
        }
    }
}

// ===== CLINIC DETAIL CARD =====

@Composable
private fun ClinicDetailCard(booking: Booking) {
    val colorScheme = MaterialTheme.colorScheme

    ReceiptCard(title = "Cơ sở y tế", icon = Icons.Outlined.LocalHospital) {
        DetailRow(
            icon = Icons.Outlined.Business,
            label = "Tên cơ sở",
            value = booking.clinicName
        )
        HorizontalDivider(
            color = colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 6.dp)
        )
        DetailRow(
            icon = Icons.Outlined.LocationOn,
            label = "Địa chỉ",
            value = buildString {
                if (booking.clinicAddress.isNotEmpty()) append(booking.clinicAddress)
                if (booking.clinicDistrict.isNotEmpty()) append(", ${booking.clinicDistrict}")
                if (booking.clinicCity.isNotEmpty()) append(", ${booking.clinicCity}")
            }.ifEmpty { "—" }
        )
        if (booking.insuranceSupported) {
            HorizontalDivider(
                color = colorScheme.outlineVariant.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorScheme.tertiaryContainer.copy(alpha = 0.5f))
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Icon(
                    Icons.Outlined.VerifiedUser,
                    contentDescription = null,
                    tint = colorScheme.tertiary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Hỗ trợ bảo hiểm y tế",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.tertiary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ===== DOCTOR DETAIL CARD =====

@Composable
private fun DoctorDetailCard(booking: Booking) {
    ReceiptCard(title = "Thông tin bác sĩ", icon = Icons.Outlined.Person) {
        DetailRow(
            icon = Icons.Outlined.Person,
            label = "Bác sĩ",
            value = booking.doctorName
        )
    }
}

// ===== PATIENT DETAIL CARD =====

@Composable
private fun PatientDetailCard(booking: Booking) {
    val colorScheme = MaterialTheme.colorScheme

    ReceiptCard(title = "Hồ sơ bệnh nhân", icon = Icons.Outlined.Person) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(colorScheme.secondaryContainer.copy(alpha = 0.5f))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorScheme.secondary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = booking.patientName.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.secondary
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    booking.patientName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.onSurface
                )
                if (booking.patientPhone.isNotEmpty()) {
                    Text(
                        booking.patientPhone,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        if (booking.patientDateOfBirth.isNotEmpty()) {
            DetailRow(
                icon = Icons.Outlined.Cake,
                label = "Ngày sinh",
                value = booking.patientDateOfBirth
            )
            HorizontalDivider(
                color = colorScheme.outlineVariant.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        if (booking.patientHealthInsurance.isNotEmpty()) {
            DetailRow(
                icon = Icons.Outlined.Badge,
                label = "Mã BHYT",
                value = booking.patientHealthInsurance
            )
            HorizontalDivider(
                color = colorScheme.outlineVariant.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        DetailRow(
            icon = Icons.Outlined.Badge,
            label = "Mã bệnh nhân",
            value = booking.patientId.ifEmpty { "—" }
        )
    }
}

// ===== PAYMENT DETAIL CARD =====

@Composable
private fun PaymentDetailCard(booking: Booking) {
    val colorScheme = MaterialTheme.colorScheme

    ReceiptCard(title = "Chi phí", icon = Icons.Outlined.Payments) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Phí khám bệnh",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurface.copy(alpha = 0.65f)
            )
            Text(
                formatPrice(booking.consultationFee),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = colorScheme.onSurface
            )
        }

        HorizontalDivider(
            color = colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        DetailRow(
            icon = Icons.Outlined.ReceiptLong,
            label = "Mã phiếu",
            value = booking.id.take(10).uppercase()
        )

        if (booking.paymentMethod.isNotEmpty()) {
            HorizontalDivider(
                color = colorScheme.outlineVariant.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            DetailRow(
                icon = Icons.Outlined.CreditCard,
                label = "Thanh toán",
                value = booking.paymentMethod
            )
        }
    }
}

// ===== QR CODE CARD =====

@Composable
private fun QrCodeCard(booking: Booking) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.primaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.QrCode,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = colorScheme.primary
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "Mã QR: ${booking.id.take(8).uppercase()}",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Đưa mã này cho lễ tân khi đến khám",
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurface.copy(alpha = 0.65f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ===== IMPORTANT NOTES CARD =====

@Composable
private fun ImportantNotesCard() {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.errorContainer.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Lưu ý quan trọng",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.error
                )
            }

            Spacer(Modifier.height(12.dp))

            val notes = listOf(
                Icons.Outlined.AccessTime to "Vui lòng đến trước 15 phút để làm thủ tục",
                Icons.Outlined.Badge to "Mang theo CCCD và thẻ BHYT (nếu có)",
                Icons.Outlined.MeetingRoom to "Số phòng khám sẽ được thông báo tại quầy lễ tân",
                Icons.Outlined.PhoneEnabled to "Liên hệ phòng khám nếu cần đổi/hủy lịch"
            )

            notes.forEachIndexed { index, (icon, text) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = colorScheme.onErrorContainer,
                        modifier = Modifier
                            .size(14.dp)
                            .offset(y = 1.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onErrorContainer,
                        lineHeight = 18.sp
                    )
                }

                if (index < notes.lastIndex) {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

// ===== REUSABLE COMPONENTS =====

@Composable
private fun ReceiptCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.primaryContainer)
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.onSurface
                )
            }

            HorizontalDivider(
                color = colorScheme.outlineVariant.copy(alpha = 0.5f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            content()
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = colorScheme.primary.copy(alpha = 0.7f),
            modifier = Modifier
                .size(14.dp)
                .offset(y = 2.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = colorScheme.onSurface.copy(alpha = 0.55f),
            modifier = Modifier.width(90.dp)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun HighlightChip(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colorScheme.primaryContainer.copy(alpha = 0.5f))
            .border(
                width = 0.5.dp,
                color = colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.height(5.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = colorScheme.onSurface.copy(alpha = 0.55f)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.onSurface
        )
    }
}

// ===== HELPERS =====

//fun formatPrice(price: Long): String {
//    if (price <= 0L) return "Miễn phí"
//    return "%,d đ".format(price).replace(',', '.')
//}
