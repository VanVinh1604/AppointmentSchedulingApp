package com.example.appointmentschedulingapp.ui.features.patient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.appointmentschedulingapp.ui.features.patient.PatientUiColors

@Composable
fun PersonalInfoSection(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    dateOfBirth: String,
    onDateOfBirthChange: (String) -> Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit
) {
    SectionCard(title = "Thông tin cá nhân") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(PatientUiColors.LightBlue)
                    .border(
                        1.5.dp,
                        PatientUiColors.BlueBorder,
                        CircleShape
                    )
                    .clickable { }
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = PatientUiColors.BlueBorder
                )
            }

            Spacer(Modifier.width(12.dp))

            ProfileTextField(
                label = "Họ và tên *",
                value = fullName,
                onChange = onFullNameChange,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProfileTextField(
                label = "Ngày sinh *",
                value = dateOfBirth,
                onChange = onDateOfBirthChange,
                placeholder = "dd/MM/yyyy",
                modifier = Modifier.weight(1f)
            )

            GenderSelector(
                selected = gender,
                onChange = onGenderChange,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(10.dp))

        ProfileTextField(
            label = "Số điện thoại *",
            value = phoneNumber,
            onChange = onPhoneChange
        )

        Spacer(Modifier.height(10.dp))

        ProfileTextField(
            label = "Địa chỉ",
            value = address,
            onChange = onAddressChange
        )
    }
}

@Composable
private fun GenderSelector(
    selected: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf("Nam", "Nữ")

    Column(modifier = modifier) {
        Text(
            "Giới tính",
            fontSize = 12.sp,
            color = PatientUiColors.SectionLabel
        )

        Spacer(Modifier.height(6.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            options.forEach { option ->
                val selectedItem = option == selected

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (selectedItem) PatientUiColors.PrimaryBlue
                            else Color.Transparent
                        )
                        .border(
                            1.dp,
                            if (selectedItem) PatientUiColors.PrimaryBlue
                            else PatientUiColors.FieldBorder,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onChange(option) }
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text(
                        option,
                        fontSize = 12.sp,
                        color = if (selectedItem) Color.White
                        else PatientUiColors.SectionLabel
                    )
                }
            }
        }
    }
}