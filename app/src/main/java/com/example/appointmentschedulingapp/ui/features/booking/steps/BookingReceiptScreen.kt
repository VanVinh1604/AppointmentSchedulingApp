package com.example.appointmentschedulingapp.ui.features.booking.steps

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.*
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
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState
import java.text.SimpleDateFormat
import java.util.*
import com.example.appointmentschedulingapp.common.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingReceiptScreen(
    uiState: BookingUiState,
    bookingId: String = "",
    onViewBooking: () -> Unit = {},
    onBackHome: () -> Unit = {}
) {
    BackHandler { onBackHome() }

    val colorScheme = MaterialTheme.colorScheme
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Phiếu xác nhận khám",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackHome) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Về trang chủ",
                            tint = colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 12.dp,
                color = colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackHome,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorScheme.primary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp
                        )
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Trang chủ", fontSize = 14.sp)
                    }

                    Button(
                        onClick = onViewBooking,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Default.EventNote,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Lịch sử khám", fontSize = 14.sp)
                    }
                }
            }
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
                SuccessHeaderCard(bookingId = bookingId)
                AppointmentTimelineCard(uiState = uiState)
                ClinicPatientCard(uiState = uiState)
                ServiceFeeCard(uiState = uiState)
                ImportantNotesCard()
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// ── Success Header ─────────────────────────────────────────────────────────────

@Composable
private fun SuccessHeaderCard(bookingId: String) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                "Đặt lịch thành công!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = colorScheme.onPrimaryContainer
            )

            Spacer(Modifier.height(4.dp))

            Text(
                "Lịch khám của bạn đã được xác nhận",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            if (bookingId.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Tag,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            "Mã phiếu: ",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorScheme.primary.copy(alpha = 0.7f)
                        )
                        Text(
                            bookingId,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// ── Appointment Timeline ───────────────────────────────────────────────────────

@Composable
private fun AppointmentTimelineCard(uiState: BookingUiState) {
    val colorScheme = MaterialTheme.colorScheme

    ReceiptCard(title = "Thông tin lịch hẹn", icon = Icons.Outlined.CalendarMonth) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HighlightChip(
                icon = Icons.Outlined.CalendarToday,
                label = "Ngày khám",
                value = formatDate(uiState.selectedDate).ifEmpty { "—" },
                modifier = Modifier.weight(1f)
            )
            HighlightChip(
                icon = Icons.Outlined.Schedule,
                label = "Buổi khám",
                value = uiState.selectedTime.ifEmpty { "—" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(2.dp))

        DetailRow(
            icon = Icons.Outlined.MedicalServices,
            label = "Chuyên khoa",
            value = uiState.selectedSpecialty.ifEmpty { "—" }
        )

        HorizontalDivider(
            color = colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 6.dp)
        )

        DetailRow(
            icon = Icons.Outlined.Healing,
            label = "Hình thức",
            value = uiState.selectedBookingType.ifEmpty { "—" }
        )
    }
}

// ── Clinic + Patient Side-by-Side ─────────────────────────────────────────────

@Composable
private fun ClinicPatientCard(uiState: BookingUiState) {
    val colorScheme = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ReceiptCard(title = "Cơ sở y tế", icon = Icons.Outlined.LocalHospital) {
            DetailRow(
                icon = Icons.Outlined.Business,
                label = "Tên cơ sở",
                value = uiState.selectedClinic?.name ?: "—"
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
                    val c = uiState.selectedClinic
                    if (c != null) {
                        if (c.address.isNotEmpty()) append(c.address)
                        if (c.district.isNotEmpty()) append(", ${c.district}")
                        if (c.city.isNotEmpty()) append(", ${c.city}")
                    } else append("—")
                }
            )
            if (uiState.selectedClinic?.insuranceSupported == true) {
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

        ReceiptCard(title = "Hồ sơ bệnh nhân", icon = Icons.Outlined.Person) {
            val patient = uiState.patientProfiles.firstOrNull { it.id == uiState.selectedPatientId }
            val displayName = patient?.fullName ?: uiState.patientName.ifEmpty { "—" }

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
                        text = displayName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colorScheme.secondary
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        displayName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = colorScheme.onSurface
                    )
                    if (patient?.phoneNumber?.isNotEmpty() == true) {
                        Text(
                            patient.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            if (patient != null) {
                Spacer(Modifier.height(8.dp))

                if (patient.dateOfBirth.isNotEmpty()) {
                    DetailRow(
                        icon = Icons.Outlined.Cake,
                        label = "Ngày sinh",
                        value = patient.dateOfBirth
                    )
                    HorizontalDivider(
                        color = colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                if (patient.healthInsuranceNumber.isNotEmpty()) {
                    DetailRow(
                        icon = Icons.Outlined.Badge,
                        label = "Mã BHYT",
                        value = patient.healthInsuranceNumber
                    )
                    HorizontalDivider(
                        color = colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }

            DetailRow(
                icon = Icons.Outlined.Badge,
                label = "Mã bệnh nhân",
                value = uiState.selectedPatientId.ifEmpty { "—" }
            )
        }
    }
}

// ── Service + Fee ──────────────────────────────────────────────────────────────

@Composable
private fun ServiceFeeCard(uiState: BookingUiState) {
    val colorScheme = MaterialTheme.colorScheme
    val fee = uiState.selectedClinic?.consultationFee ?: 0L

    ReceiptCard(title = "Chi phí dự kiến", icon = Icons.Outlined.Payments) {
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
                formatPrice(fee),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = colorScheme.onSurface
            )
        }

        HorizontalDivider(
            color = colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(colorScheme.tertiaryContainer.copy(alpha = 0.6f))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Payments,
                    contentDescription = null,
                    tint = colorScheme.tertiary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Tổng thanh toán",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.tertiary
                )
            }
            Text(
                formatPrice(fee),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.tertiary
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            "* Chi phí thực tế có thể thay đổi theo chỉ định của bác sĩ",
            style = MaterialTheme.typography.labelSmall,
            color = colorScheme.onSurface.copy(alpha = 0.45f),
            modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}

// ── Important Notes ────────────────────────────────────────────────────────────

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
        border = CardDefaults.outlinedCardBorder().copy(
            width = 0.5.dp
        )
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

// ── Reusable Components ────────────────────────────────────────────────────────

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
            color = colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.primary
        )
    }
}

// ── Helpers ────────────────────────────────────────────────────────────────────

private fun formatDate(dateString: String): String {
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val output = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        output.format(input.parse(dateString)!!)
    } catch (_: Exception) {
        try {
            val input2 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            input2.parse(dateString)
            dateString
        } catch (_: Exception) {
            dateString
        }
    }
}

//private fun formatPrice(price: Long): String {
//    if (price <= 0L) return "Miễn phí"
//    return "%,d đ".format(price).replace(',', '.')
//}