package com.example.appointmentschedulingapp.ui.components.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun AppScreen(
    requiresLogin: Boolean = false,
    isLoggedIn: Boolean, // Lấy từ ViewModel hoặc LocalUser
    onNavigateToLogin: () -> Unit,
    content: @Composable () -> Unit
) {
    var showLoginDialog by remember { mutableStateOf(false) }

    // Kiểm tra ngay khi màn hình được khởi tạo
    LaunchedEffect(requiresLogin, isLoggedIn) {
        if (requiresLogin && !isLoggedIn) {
            showLoginDialog = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Luôn hiển thị nội dung màn hình (hoặc ẩn đi nếu muốn gắt gao hơn)
        content()

        // Lớp phủ Dialog
        LoginRequiredDialog(
            show = showLoginDialog,
            onDismiss = { showLoginDialog = false },
            onLoginClick = {
                showLoginDialog = false
                onNavigateToLogin()
            }
        )
    }
}