package com.example.appointmentschedulingapp.ui.features.booking.steps.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Color tokens ─────────────────────────────────────────────────────────────
private val PrimaryBlue  = Color(0xFF1565C0)
private val LightBlue    = Color(0xFFE3F2FD)
private val SectionLabel = Color(0xFF546E7A)
private val FieldBorder  = Color(0xFFE0E0E0)
private val FieldBg      = Color(0xFFF5F7FA)

// ─── Data model ───────────────────────────────────────────────────────────────
// Thêm phương thức thanh toán mới chỉ cần thêm vào list DEFAULT_PAYMENT_METHODS
// hoặc truyền list tùy chỉnh qua tham số availableMethods

data class PaymentMethodOption(
    val id: String,
    val label: String,
    val description: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color,
    val isImplemented: Boolean = false,
    val group: String = "Khác"         // dùng để group theo danh mục
)

// ─── Default methods — thêm/bớt tại đây ──────────────────────────────────────
val DEFAULT_PAYMENT_METHODS: List<PaymentMethodOption> = listOf(

    // ── Ví điện tử ────────────────────────────────────────────────────────────
    PaymentMethodOption(
        id          = "MOMO",
        label       = "Ví MoMo",
        description = "Thanh toán qua ứng dụng MoMo",
        icon        = Icons.Default.AccountBalanceWallet,
        iconTint    = Color(0xFFAE2070),
        iconBg      = Color(0xFFFCE4EC),
        group       = "Ví điện tử",
        isImplemented = true
    ),
    PaymentMethodOption(
        id          = "ZALOPAY",
        label       = "ZaloPay",
        description = "Thanh toán qua ứng dụng ZaloPay",
        icon        = Icons.Default.AccountBalanceWallet,
        iconTint    = Color(0xFF0068FF),
        iconBg      = Color(0xFFE8F0FF),
        group       = "Ví điện tử",
        isImplemented = false
    ),
    PaymentMethodOption(
        id          = "VNPAY_WALLET",
        label       = "Ví VNPAY",
        description = "Thanh toán qua ví VNPAY",
        icon        = Icons.Default.AccountBalanceWallet,
        iconTint    = Color(0xFF003087),
        iconBg      = Color(0xFFE8EAF6),
        group       = "Ví điện tử",
        isImplemented = false
    ),
    PaymentMethodOption(
        id          = "SHOPEEPAY",
        label       = "ShopeePay",
        description = "Thanh toán qua ví ShopeePay",
        icon        = Icons.Default.AccountBalanceWallet,
        iconTint    = Color(0xFFEE4D2D),
        iconBg      = Color(0xFFFFF0ED),
        group       = "Ví điện tử",
        isImplemented = false
    ),

    // ── Thẻ / Ngân hàng ───────────────────────────────────────────────────────
    PaymentMethodOption(
        id          = "VNPAY_CARD",
        label       = "VNPAY – Thẻ ATM",
        description = "Thẻ ATM nội địa có Internet Banking",
        icon        = Icons.Default.CreditCard,
        iconTint    = Color(0xFF003087),
        iconBg      = Color(0xFFE8EAF6),
        group       = "Thẻ & Ngân hàng",
        isImplemented = false

    ),
    PaymentMethodOption(
        id          = "VISA_MASTER",
        label       = "Visa / MasterCard",
        description = "Thẻ quốc tế Visa, MasterCard, JCB",
        icon        = Icons.Default.CreditCard,
        iconTint    = Color(0xFF1A237E),
        iconBg      = Color(0xFFE8EAF6),
        group       = "Thẻ & Ngân hàng",
        isImplemented = false
    ),
    PaymentMethodOption(
        id          = "BANK_TRANSFER",
        label       = "Chuyển khoản ngân hàng",
        description = "Chuyển khoản trực tiếp qua số tài khoản",
        icon        = Icons.Default.AccountBalance,
        iconTint    = Color(0xFF1B5E20),
        iconBg      = Color(0xFFE8F5E9),
        group       = "Thẻ & Ngân hàng",
        isImplemented = false
    ),

    // ── Bảo hiểm ──────────────────────────────────────────────────────────────
    PaymentMethodOption(
        id          = "BHYT",
        label       = "Bảo hiểm y tế (BHYT)",
        description = "Thanh toán qua thẻ bảo hiểm y tế",
        icon        = Icons.Default.VerifiedUser,
        iconTint    = Color(0xFF00695C),
        iconBg      = Color(0xFFE0F2F1),
        group       = "Bảo hiểm",
        isImplemented = false
    ),
    PaymentMethodOption(
        id          = "INSURANCE_PRIVATE",
        label       = "Bảo hiểm thương mại",
        description = "Bảo Việt, PVI, Prudential, AIA...",
        icon        = Icons.Default.Shield,
        iconTint    = Color(0xFF1565C0),
        iconBg      = Color(0xFFE3F2FD),
        group       = "Bảo hiểm",
        isImplemented = false
    ),

    // ── Tiền mặt ──────────────────────────────────────────────────────────────
    PaymentMethodOption(
        id          = "CASH",
        label       = "Tiền mặt tại quầy",
        description = "Nộp tiền mặt khi đến làm thủ tục",
        icon        = Icons.Default.Payments,
        iconTint    = Color(0xFF1B5E20),
        iconBg      = Color(0xFFE8F5E9),
        group       = "Tiền mặt",
        isImplemented = true

    )
)

// ─── PaymentMethodSelector ────────────────────────────────────────────────────
// Composable tái sử dụng được — truyền list tùy chỉnh hoặc dùng default
@Composable
fun PaymentMethodSelector(
    selectedId: String,
    onSelect: (PaymentMethodOption) -> Unit,
    modifier: Modifier = Modifier,
    availableMethods: List<PaymentMethodOption> = DEFAULT_PAYMENT_METHODS
) {
    var query by remember { mutableStateOf("") }

    val grouped = remember(query, availableMethods) {
        availableMethods
            .filter {
                query.isBlank() ||
                        it.label.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.group.contains(query, ignoreCase = true)
            }
            .groupBy { it.group }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // ── Search bar ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(FieldBg)
                .border(0.5.dp, FieldBorder, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint     = SectionLabel,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Box(Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        "Tìm phương thức thanh toán...",
                        fontSize = 13.sp,
                        color    = SectionLabel
                    )
                }
                BasicTextField(
                    value         = query,
                    onValueChange = { query = it },
                    textStyle     = TextStyle(fontSize = 13.sp, color = Color(0xFF1A1A1A)),
                    cursorBrush   = SolidColor(PrimaryBlue),
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth()
                )
            }
            if (query.isNotEmpty()) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Xóa",
                    tint     = SectionLabel,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { query = "" }
                )
            }
        }

        // ── Empty state ───────────────────────────────────────────────────────
        if (grouped.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = null,
                        tint     = FieldBorder,
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        "Không tìm thấy phương thức",
                        fontSize = 13.sp,
                        color    = SectionLabel
                    )
                }
            }
        }

        // ── Grouped list ──────────────────────────────────────────────────────
        grouped.forEach { (group, methods) ->
            PaymentGroupSection(
                groupName = group,
                methods   = methods,
                selectedId = selectedId,
                onSelect  = onSelect
            )
        }
    }
}

// ─── Group section ────────────────────────────────────────────────────────────
@Composable
private fun PaymentGroupSection(
    groupName: String,
    methods: List<PaymentMethodOption>,
    selectedId: String,
    onSelect: (PaymentMethodOption) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Column {
        // Group header — bấm để collapse/expand
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                groupName,
                fontSize      = 11.sp,
                fontWeight    = FontWeight.SemiBold,
                color         = SectionLabel,
                letterSpacing = 0.4.sp
            )
            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint     = SectionLabel,
                modifier = Modifier.size(16.dp)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter   = fadeIn() + expandVertically(),
            exit    = fadeOut() + shrinkVertically()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                methods.forEach { method ->
                    PaymentMethodRow(
                        method   = method,
                        selected = method.id == selectedId,
                        onClick  = { onSelect(method) }
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))
    }
}

// ─── Single method row ────────────────────────────────────────────────────────
@Composable
private fun PaymentMethodRow(
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
        // Icon
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .background(method.iconBg, RoundedCornerShape(10.dp))
        ) {
            Icon(
                method.icon,
                contentDescription = null,
                tint     = method.iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        // Label + description
        Column(Modifier.weight(1f)) {
            Text(
                method.label,
                fontWeight = FontWeight.Medium,
                fontSize   = 13.sp,
                color      = if (selected) PrimaryBlue else Color(0xFF1A1A1A)
            )
            Text(
                method.description,
                fontSize = 11.sp,
                color    = SectionLabel
            )
        }

        // Radio dot
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(20.dp)
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
                        .size(7.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                )
            }
        }
    }
}