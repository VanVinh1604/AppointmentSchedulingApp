package com.example.appointmentschedulingapp.ui.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpVerificationScreen(
    phoneNumber: String, // Nhận số điện thoại từ màn trước truyền sang
    onBack: () -> Unit,
    onVerify: (String) -> Unit,
    onResendOtp: () -> Unit,
    resendSeconds: Int,
    isLoading: Boolean = false,       // ← thêm
    errorMessage: String? = null,
    ) {
    val primaryColor = Color(0xFF1976D2)
    // List chứa 6 giá trị của mã OTP
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // --- TOP ROW: BACK & SUPPORT ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { /* Gọi tổng đài */ }
            ) {
                Icon(Icons.Default.PhoneInTalk, null, tint = primaryColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Gọi hỗ trợ", color = primaryColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }

        // --- CENTER CONTENT ---
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MEDPRO",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = primaryColor,
                letterSpacing = 2.sp
            )
            Text(
                text = "ĐẶT LỊCH KHÁM BỆNH",
                fontSize = 16.sp,
                color = primaryColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Nhập mã xác thực OTP",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Mở Zalo hoặc tin nhắn SMS của số điện thoại $phoneNumber để lấy mã xác thực OTP",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- 6 OTP INPUT FIELDS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                otpValues.forEachIndexed { index, value ->
                    OtpBox(
                        value = value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 1) {
                                otpValues[index] = newValue
                                // Logic tự động chuyển focus hoặc gọi verify khi nhập đủ 6 số có thể thêm ở đây
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- RESEND TEXT ---
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bạn không nhận được mã? ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                if (resendSeconds > 0) {
                    Text(
                        text = "Gửi lại sau ${resendSeconds}s",
                        color = Color.Gray
                    )
                } else {
                    Text(
                        text = "Gửi lại mã",
                        color = primaryColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onResendOtp() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Button xác nhận cuối cùng
            Button(
                onClick = { onVerify(otpValues.joinToString("")) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                enabled = otpValues.all { it.isNotEmpty() } && !isLoading  // ← disable khi loading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text("XÁC NHẬN", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            errorMessage?.let { msg ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = msg,
                    color = Color.Red,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun OtpBox(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 1) onValueChange(it)
        },
        modifier = Modifier.size(45.dp, 58.dp), // Tăng nhẹ chiều cao để không bị cắt số
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            // Nếu dùng bản M3 mới, hãy dùng outlineColor thay vì borderColor
            focusedBorderColor = Color(0xFF1976D2),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color(0xFFF9F9F9),
            unfocusedContainerColor = Color(0xFFF9F9F9),
            cursorColor = Color(0xFF1976D2)
        )
    )
}