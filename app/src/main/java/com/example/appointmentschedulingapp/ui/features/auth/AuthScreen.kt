
package com.example.appointmentschedulingapp.ui.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
fun AuthScreen(
    onBack: () -> Unit,
    onContinue: (String) -> Unit,
    isLoading: Boolean = false,        // ← thêm dòng này
    errorMessage: String? = null,
) {
    var phoneNumber by remember { mutableStateOf("") }
    val primaryColor = Color(0xFF1976D2)

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
                Icon(
                    Icons.Default.PhoneInTalk,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Gọi hỗ trợ",
                    color = primaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // --- CENTER VIEW: LOGO & TEXT ---
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
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
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Nhập số điện thoại để tạo tài khoản và đăng nhập",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- INPUT ROW: COUNTRY CODE & PHONE NUMBER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Country Selector Box
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                        .clickable { /* Mở bottom sheet chọn quốc gia */ }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bạn có thể thay Image bằng Icon nếu chưa có hình cờ
                    Box(modifier = Modifier.size(24.dp).background(Color.LightGray)) // Demo cờ VN
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "+84", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Phone Input Field
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier.fillMaxSize(),
                    placeholder = { Text("Số điện thoại") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- CONTINUE BUTTON ---
            Button(
                onClick = { onContinue(phoneNumber) },
                enabled = phoneNumber.isNotEmpty() && !isLoading && phoneNumber.length >= 9 ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text("TIẾP TỤC", fontWeight = FontWeight.Bold, color = Color.White)
                }            }
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 13.sp
                )
            }
        }

        // Khoảng trống dưới cùng để cân bằng giao diện
        Spacer(modifier = Modifier.height(40.dp))
    }
}