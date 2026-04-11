package com.example.appointmentschedulingapp.ui.features.booking.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState
import com.example.appointmentschedulingapp.ui.features.booking.components.BookingFlowTopBar

import com.example.appointmentschedulingapp.ui.features.booking.steps.components.DEFAULT_PAYMENT_METHODS
import com.example.appointmentschedulingapp.ui.features.booking.steps.components.PaymentMethodOption
import com.example.appointmentschedulingapp.ui.features.booking.steps.components.PaymentMethodSelector

// ─── Color tokens ─────────────────────────────────────────────────────────────
private val PrimaryBlue  = Color(0xFF1565C0)
private val LightBlue    = Color(0xFFE3F2FD)
private val SectionLabel = Color(0xFF546E7A)
private val FieldBorder  = Color(0xFFE0E0E0)
private val DividerColor = Color(0xFFF0F0F0)
private val GreenSuccess = Color(0xFF2E7D32)
private val GreenBg      = Color(0xFFE8F5E9)

// 3 phương thức gợi ý cố định trên màn hình chính
private val PINNED_METHOD_IDS = listOf("MOMO", "VNPAY_CARD", "CASH")

// ─── Screen ───────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingStep4Screen(
    uiState: BookingUiState,
    onBack: () -> Unit,
    onPaymentSelected: (String) -> Unit,
    onConfirmPayment: () -> Unit,
    onNavigateToReceipt: () -> Unit,
    availableMethods: List<PaymentMethodOption> = DEFAULT_PAYMENT_METHODS
) {

    var selectedMethod by remember {
        mutableStateOf(
            availableMethods.firstOrNull { it.id == "CASH" } ?: availableMethods.first()
        )
    }
    var showAllSheet by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToReceipt()
        }
    }
    val pinnedMethods = remember(availableMethods) {
        PINNED_METHOD_IDS.mapNotNull { id -> availableMethods.firstOrNull { it.id == id } }
    }

    // ── Bottom sheet: tất cả phương thức + search ────────────────────────────
    if (showAllSheet) {
        PaymentBottomSheet(
            availableMethods = availableMethods,
            selectedId       = selectedMethod.id,
            onSelect         = { method ->
                selectedMethod = method
                showAllSheet   = false
            },
            onDismiss = { showAllSheet = false }
        )
    }

    Scaffold(
        topBar = {
            BookingFlowTopBar(
                title       = "Thanh toán",
                currentStep = 4,
                onBack      = onBack
            )
        },
        bottomBar = {
            BottomPayBar(
                method    = selectedMethod,
                amount    = uiState.selectedClinic?.consultationFee ?: 0L,
                onConfirm = {
                    onPaymentSelected(selectedMethod.id)
                    onConfirmPayment()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ── Recap ────────────────────────────────────────────────────────
            BookingRecapCard(uiState = uiState)

            // ── Section label ────────────────────────────────────────────────
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                modifier              = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(Modifier.weight(1f), color = FieldBorder, thickness = 0.5.dp)
                Text(
                    "  Phương thức thanh toán  ",
                    fontSize = 12.sp,
                    color    = SectionLabel
                )
                HorizontalDivider(Modifier.weight(1f), color = FieldBorder, thickness = 0.5.dp)
            }

            // ── 3 pinned suggestions ─────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                pinnedMethods.forEach { method ->
                    PinnedPaymentCard(
                        method   = method,
                        selected = method.id == selectedMethod.id,
                        onClick  = { selectedMethod = method }
                    )
                }
            }

            // ── "Xem thêm" button ────────────────────────────────────────────
            OutlinedButton(
                onClick  = { showAllSheet = true },
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue),
                border   = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue)
            ) {
                Icon(
                    Icons.Default.GridView,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Xem tất cả phương thức thanh toán",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Nếu đang chọn method KHÔNG nằm trong pinned → hiện thêm 1 row
            if (pinnedMethods.none { it.id == selectedMethod.id }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(LightBlue)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint     = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Đã chọn: ${selectedMethod.label}",
                        fontSize   = 13.sp,
                        color      = PrimaryBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // ── Security note ────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F7FA), RoundedCornerShape(10.dp))
                    .border(0.5.dp, FieldBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint     = SectionLabel,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Thông tin thanh toán được mã hóa và bảo mật an toàn.",
                    fontSize   = 12.sp,
                    color      = SectionLabel,
                    lineHeight = 17.sp
                )
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}

// ─── Pinned payment card ──────────────────────────────────────────────────────
@Composable
private fun PinnedPaymentCard(
    method: PaymentMethodOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) LightBlue else Color.White)
            .border(
                width = if (selected) 1.5.dp else 0.5.dp,
                color = if (selected) PrimaryBlue else FieldBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .background(method.iconBg, RoundedCornerShape(10.dp))
        ) {
            Icon(
                method.icon,
                contentDescription = null,
                tint     = method.iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                method.label,
                fontWeight = FontWeight.Medium,
                fontSize   = 14.sp,
                color      = if (selected) PrimaryBlue else Color(0xFF1A1A1A)
            )
            Text(
                method.description,
                fontSize = 12.sp,
                color    = SectionLabel
            )
        }

        // Radio dot
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(50))
                .background(if (selected) PrimaryBlue else Color.Transparent)
                .border(
                    width = if (selected) 0.dp else 1.5.dp,
                    color = if (selected) PrimaryBlue else FieldBorder,
                    shape = RoundedCornerShape(50)
                )
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                )
            }
        }
    }
}

// ─── Bottom sheet: tất cả phương thức + tìm kiếm ─────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentBottomSheet(
    availableMethods: List<PaymentMethodOption>,
    selectedId: String,
    onSelect: (PaymentMethodOption) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest    = onDismiss,
        sheetState          = sheetState,
        containerColor      = Color.White,
        shape               = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle          = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FieldBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            // Sheet header
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Tất cả phương thức",
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 16.sp,
                    color      = Color(0xFF1A1A1A)
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Đóng",
                        tint = SectionLabel
                    )
                }
            }

            HorizontalDivider(color = FieldBorder, thickness = 0.5.dp)
            Spacer(Modifier.height(12.dp))

            // PaymentMethodSelector — có search + group collapse
            PaymentMethodSelector(
                selectedId       = selectedId,
                onSelect         = onSelect,
                availableMethods = availableMethods
            )
        }
    }
}

// ─── Bottom pay bar ───────────────────────────────────────────────────────────
@Composable
private fun BottomPayBar(
    method: PaymentMethodOption,
    amount: Long,
    onConfirm: () -> Unit
) {
    Surface(shadowElevation = 8.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        method.icon,
                        contentDescription = null,
                        tint     = method.iconTint,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(method.label, fontSize = 13.sp, color = SectionLabel)
                }
                Text(
                    formatCurrency(amount),
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color      = GreenSuccess
                )
            }

            Spacer(Modifier.height(10.dp))

            Button(
                onClick  = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape  = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "THANH TOÁN NGAY",
                    fontSize      = 15.sp,
                    fontWeight    = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// ─── Recap card ───────────────────────────────────────────────────────────────
@Composable
private fun BookingRecapCard(uiState: BookingUiState) {
    val patient = uiState.patientProfiles
        .firstOrNull { it.id == uiState.selectedPatientId }
    val patientDisplayName = patient?.fullName ?: uiState.patientName.ifEmpty { "—" }
    val fee = uiState.selectedClinic?.consultationFee ?: 0L

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp),
        border    = androidx.compose.foundation.BorderStroke(0.5.dp, FieldBorder)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .background(LightBlue, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        Icons.Default.EventAvailable,
                        contentDescription = null,
                        tint     = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    "Thông tin lịch hẹn",
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = Color(0xFF1A1A1A)
                )
            }

            HorizontalDivider(
                color     = DividerColor,
                thickness = 0.5.dp,
                modifier  = Modifier.padding(vertical = 10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightBlue, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text(
                    uiState.selectedClinic?.name ?: "—",
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = PrimaryBlue
                )
                if (uiState.selectedSpecialty.isNotEmpty()) {
                    Text(uiState.selectedSpecialty, fontSize = 12.sp, color = SectionLabel)
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier              = Modifier.fillMaxWidth()
            ) {
                InfoChip(Icons.Default.CalendarMonth, uiState.selectedDate.ifEmpty { "—" }, Modifier.weight(1f))
                InfoChip(Icons.Default.AccessTime, uiState.selectedTime.ifEmpty { "—" }, Modifier.weight(1f))
            }

            Spacer(Modifier.height(8.dp))

            RecapRow(Icons.Default.Person, "Bệnh nhân", patientDisplayName)

            if (uiState.selectedBookingType.isNotEmpty()) {
                HorizontalDivider(color = DividerColor, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                RecapRow(Icons.Default.MedicalServices, "Hình thức", uiState.selectedBookingType)
            }

            HorizontalDivider(color = DividerColor, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenBg, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Payments, null, tint = GreenSuccess, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Số tiền cần thanh toán", fontSize = 13.sp, color = GreenSuccess, fontWeight = FontWeight.Medium)
                }
                Text(formatCurrency(fee), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = GreenSuccess)
            }
        }
    }
}

// ─── Atoms ────────────────────────────────────────────────────────────────────
@Composable
private fun InfoChip(icon: ImageVector, label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color(0xFFF5F7FA), RoundedCornerShape(8.dp))
            .border(0.5.dp, FieldBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(13.dp))
        Spacer(Modifier.width(5.dp))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A))
    }
}

@Composable
private fun RecapRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier          = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = SectionLabel, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, color = SectionLabel, modifier = Modifier.width(80.dp))
        Text(
            value,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Medium,
            color      = Color(0xFF1A1A1A),
            textAlign  = TextAlign.End,
            modifier   = Modifier.weight(1f)
        )
    }
}

private fun formatCurrency(amount: Long): String {
    if (amount <= 0L) return "Miễn phí"
    return "%,d đ".format(amount).replace(',', '.')
}