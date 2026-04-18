package com.example.appointmentschedulingapp.ui.components.login

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun LoginRequiredDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onLoginClick: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Yêu cầu đăng nhập") },
            text = { Text("Bạn cần đăng nhập để sử dụng tính năng này. Tiếp tục?") },
            confirmButton = {
                Button(onClick = onLoginClick) {
                    Text("Đăng nhập ngay")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Để sau")
                }
            },
            shape = RoundedCornerShape(8.dp) // Hình chữ nhật bo góc nhẹ
        )
    }
}