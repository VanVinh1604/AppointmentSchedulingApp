package com.example.appointmentschedulingapp.ui.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val itemsPerPage = 8
    val pages = actions.chunked(itemsPerPage)
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top // Cố định điểm bắt đầu từ phía trên
            ) { pageIndex ->
                val currentPageItems = pages[pageIndex]
                // Chia thành tối đa 2 dòng (mỗi dòng 4 item)
                val rows = currentPageItems.chunked(4)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // 1. CỐ ĐỊNH CHIỀU CAO CHO 2 DÒNG (Điều chỉnh số này cho phù hợp UI của bạn)
                ) {
                    // Render dòng 1 và dòng 2
                    for (rowIndex in 0..1) {
                        val rowItems = rows.getOrNull(rowIndex)

                        if (rowItems != null) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                rowItems.forEach { action ->
                                    ActionItem(
                                        action = action,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            // KIỂM TRA AN TOÀN: Chỉ click nếu iconName/route hợp lệ
                                            if (action.iconName.isNotEmpty()) {
                                                onActionClick(action)
                                            }
                                        }
                                    )
                                }
                                // Fill nốt khoảng trống nếu dòng 1/2 không đủ 4 item
                                if (rowItems.size < 4) {
                                    repeat(4 - rowItems.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        } else {
                            // 2. NẾU DÒNG KHÔNG TỒN TẠI (trang cuối chỉ có 1 dòng)
                            // Vẫn tạo 1 Row trống với chiều cao tương đương ActionItem để giữ Card không bị co lại
                            Box(modifier = Modifier.fillMaxWidth().weight(1f))
                        }

                        if (rowIndex == 0) Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            // Indicator Dot
            if (pages.size > 1) {
                Spacer(modifier = Modifier.height(8.dp)) // Tạo khoảng cách nhỏ với grid ở trên
                Row(
                    Modifier.height(10.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pages.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color(0xFF1976D2) else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionItem(
    action: HomeAction,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    val clickableModifier = if (action.iconName.isNotEmpty()) {
        Modifier.clickable { onClick() }
    } else {
        Modifier // Không cho phép click nếu không có dữ liệu
    }
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
            maxLines = 2,
            minLines = 2 // Đảm bảo các text luôn cao bằng nhau để icon thẳng hàng
        )
    }
}

// Giữ nguyên hàm getIconFromName của bạn...
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