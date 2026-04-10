package com.example.appointmentschedulingapp.ui.features.booking.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointmentschedulingapp.ui.features.booking.BookingUiState
import com.example.appointmentschedulingapp.ui.features.booking.components.BookingFlowTopBar

// ─── Color tokens ─────────────────────────────────────────────────────────────
private val PrimaryBlue  = Color(0xFF1565C0)
private val LightBlue    = Color(0xFFE3F2FD)
private val SectionLabel = Color(0xFF546E7A)
private val FieldBorder  = Color(0xFFE0E0E0)
private val DividerColor = Color(0xFFF0F0F0)
private val GreenSuccess = Color(0xFF2E7D32)
private val GreenBg      = Color(0xFFE8F5E9)
private val AmberWarning = Color(0xFFE65100)
private val AmberBg      = Color(0xFFFFF8E1)

// ─── Screen ───────────────────────────────────────────────────────────────────
@Composable
fun BookingStep3Screen(
    uiState: BookingUiState,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    Scaffold(
        topBar = {
            BookingFlowTopBar(
                title = "Xác nhận đặt khám",
                currentStep = 3,
                onBack = onBack
            )
        },
        bottomBar = {
            BottomConfirmBar(onConfirm = onConfirm)
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

            // ── Hint banner ─────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightBlue, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Vui lòng kiểm tra lại thông tin trước khi xác nhận đặt khám.",
                    fontSize = 13.sp,
                    color = PrimaryBlue,
                    lineHeight = 18.sp
                )
            }

            // ── 1. Cơ sở y tế ───────────────────────────────────────────────
            SummaryCard(
                title    = "Cơ sở y tế",
                icon     = Icons.Default.LocalHospital,
                iconTint = PrimaryBlue
            ) {
                SummaryRow(
                    label = "Tên cơ sở",
                    value = uiState.selectedClinic?.name ?: "—"
                )
                SummaryDivider()
                SummaryRow(
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
                SummaryDivider()
                SummaryRow(
                    label = "Chuyên khoa",
                    value = uiState.selectedSpecialty.ifEmpty { "—" }
                )
                if (uiState.selectedClinic?.insuranceSupported == true) {
                    SummaryDivider()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = GreenSuccess,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Hỗ trợ bảo hiểm y tế",
                            fontSize = 12.sp,
                            color = GreenSuccess,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // ── 2. Thông tin dịch vụ ────────────────────────────────────────
            SummaryCard(
                title    = "Thông tin dịch vụ",
                icon     = Icons.Default.MedicalServices,
                iconTint = PrimaryBlue
            ) {
                SummaryRow(
                    label = "Hình thức khám",
                    value = uiState.selectedBookingType.ifEmpty { "—" }
                )
                SummaryDivider()
                SummaryRow(
                    label = "Ngày khám",
                    value = uiState.selectedDate.ifEmpty { "—" }
                )
                SummaryDivider()
                SummaryRow(
                    label = "Giờ khám",
                    value = uiState.selectedTime.ifEmpty { "—" }
                )
//                if (uiState.selectedDoctor != null) {
//                    SummaryDivider()
//                    SummaryRow(
//                        label = "Bác sĩ",
//                        value = uiState.selectedDoctor.name.ifEmpty { "—" }
//                    )
//                }
            }

            // ── 3. Hồ sơ bệnh nhân ──────────────────────────────────────────
            SummaryCard(
                title    = "Hồ sơ bệnh nhân",
                icon     = Icons.Default.Person,
                iconTint = PrimaryBlue
            ) {
                // Ưu tiên tìm trong danh sách patientProfiles, fallback về patientName
                val patient = uiState.patientProfiles
                    .firstOrNull { it.id == uiState.selectedPatientId }

                SummaryRow(
                    label = "Họ và tên",
                    value = patient?.fullName ?: uiState.patientName.ifEmpty { "—" }
                )
                if (patient != null) {
                    if (patient.phoneNumber.isNotEmpty()) {
                        SummaryDivider()
                        SummaryRow("Số điện thoại", patient.phoneNumber)
                    }
                    if (patient.dateOfBirth.isNotEmpty()) {
                        SummaryDivider()
                        SummaryRow("Ngày sinh", patient.dateOfBirth)
                    }
                    if (patient.healthInsuranceNumber.isNotEmpty()) {
                        SummaryDivider()
                        SummaryRow("Mã BHYT", patient.healthInsuranceNumber)
                    }
                    if (patient.relationship.isNotEmpty() && patient.relationship != "Bản thân") {
                        SummaryDivider()
                        SummaryRow("Quan hệ với chủ tài khoản", patient.relationship)
                    }
                }
            }

            SummaryCard(
                title = "Dịch vụ bổ sung",
                icon = Icons.Default.AddCircle,
                iconTint = PrimaryBlue
            ) {
                AdditionalServiceItem(
                    title = "Ưu tiên khám nhanh",
                    price = 50000
                )

                SummaryDivider()

                AdditionalServiceItem(
                    title = "Tư vấn sau khám",
                    price = 100000
                )

                SummaryDivider()

                AdditionalServiceItem(
                    title = "Nhận kết quả tại nhà",
                    price = 30000
                )
            }

            // ── 4. Chi phí dự kiến ──────────────────────────────────────────
            SummaryCard(
                title    = "Chi phí dự kiến",
                icon     = Icons.Default.Payments,
                iconTint = GreenSuccess
            ) {
                val fee = uiState.selectedClinic?.consultationFee ?: 0L

                SummaryRow(
                    label = "Phí khám bệnh",
                    value = formatCurrency(fee)
                )
                SummaryDivider()
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GreenBg, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Tổng thanh toán",
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = GreenSuccess
                    )
                    Text(
                        formatCurrency(fee),
                        fontWeight = FontWeight.Bold,
                        fontSize   = 17.sp,
                        color      = GreenSuccess
                    )
                }
                Text(
                    "* Chi phí thực tế có thể thay đổi theo chỉ định của bác sĩ",
                    fontSize = 11.sp,
                    color    = SectionLabel,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // ── 5. Lưu ý ────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AmberBg, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint     = AmberWarning,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        "Lưu ý quan trọng",
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 13.sp,
                        color      = AmberWarning
                    )
                    Text(
                        "• Vui lòng đến trước giờ hẹn 15 phút để làm thủ tục.\n" +
                                "• Mang theo CCCD và thẻ BHYT (nếu có).\n" +
                                "• Số phòng khám sẽ được thông báo tại quầy lễ tân.",
                        fontSize   = 12.sp,
                        color      = Color(0xFF5D4037),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}

// ─── Bottom bar ───────────────────────────────────────────────────────────────
@Composable
private fun BottomConfirmBar(onConfirm: () -> Unit) {
    Surface(shadowElevation = 8.dp) {
        Button(
            onClick  = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                "Tiếp Tục",
                fontSize      = 15.sp,
                fontWeight    = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// ─── Reusable atoms ───────────────────────────────────────────────────────────
@Composable
private fun SummaryCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border    = androidx.compose.foundation.BorderStroke(0.5.dp, FieldBorder)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = iconTint.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint     = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = Color(0xFF1A1A1A)
                )
            }
            HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.Top
    ) {
        Text(
            label,
            fontSize = 13.sp,
            color    = SectionLabel,
            modifier = Modifier.weight(0.42f)
        )
        Text(
            value,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Medium,
            color      = Color(0xFF1A1A1A),
            textAlign  = TextAlign.End,
            modifier   = Modifier.weight(0.58f)
        )
    }
}

@Composable
private fun SummaryDivider() {
    HorizontalDivider(
        color     = DividerColor,
        thickness = 0.5.dp,
        modifier  = Modifier.padding(vertical = 1.dp)
    )
}

// VND formatter — dùng Long tránh floating point
private fun formatCurrency(amount: Long): String {
    if (amount <= 0L) return "Miễn phí"
    return "%,d đ".format(amount).replace(',', '.')
}

@Composable
private fun AdditionalServiceItem(
    title: String,
    price: Long
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = false,
                onCheckedChange = {}
            )
            Text(title, fontSize = 13.sp)
        }

        Text(
            formatCurrency(price),
            fontWeight = FontWeight.SemiBold,
            color = PrimaryBlue
        )
    }
}