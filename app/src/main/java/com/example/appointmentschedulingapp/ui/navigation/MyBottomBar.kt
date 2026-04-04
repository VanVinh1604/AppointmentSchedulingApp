package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MyBottomBar(navController: NavHostController) {
    val primaryColor = Color(0xFF1976D2)

    val mainTabs = listOf(
        Screen.Home.route,
        Screen.Profile.route,
        Screen.Ticket.route,
        Screen.Notification.route,
        Screen.Account.route
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val showBottomBar = currentRoute in mainTabs

    if (showBottomBar) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier.height(80.dp)
        ) {
            val items = listOf(
                Screen.Home,
                Screen.Profile,
                Screen.Ticket,
                Screen.Notification,
                Screen.Account
            )

            items.forEach { screen ->
                val isSelected = currentRoute == screen.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Vạch xanh phía trên
                            Box(
                                modifier = Modifier
                                    .width(28.dp)
                                    .height(3.dp)
                                    .background(if (isSelected) primaryColor else Color.Transparent)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                // Tiết chế độ phóng to: 26dp thay vì 28dp để tránh lấn không gian chữ
                                modifier = Modifier.size(if (isSelected) 26.dp else 24.dp)
                            )
                        }
                    },
                    label = {
                        // TỐI ƯU TẠI ĐÂY:
                        Text(
                            text = screen.title,
                            // Giảm nhẹ size chữ khi chọn để không bị quá to gây xuống dòng
                            fontSize = if (isSelected) 11.sp else 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1, // Ép buộc chỉ trên 1 dòng
                            softWrap = false, // Không tự động xuống dòng
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis, // Hiển thị "..." nếu dài
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = primaryColor,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent // Loại bỏ hẳn vùng nền xám khi chọn
                    )
                )
            }
        }
    }
}