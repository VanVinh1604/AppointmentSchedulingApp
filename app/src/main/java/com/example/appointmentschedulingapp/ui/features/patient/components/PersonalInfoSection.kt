package com.example.appointmentschedulingapp.ui.features.patient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.appointmentschedulingapp.domain.model.location.Province
import com.example.appointmentschedulingapp.domain.model.location.Ward
import com.example.appointmentschedulingapp.ui.features.patient.PatientUiColors

@OptIn(ExperimentalMaterial3Api::class)
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

    provinces: List<Province>,
    selectedProvince: Province?,
    onProvinceSelected: (Province) -> Unit,

    wards: List<Ward>,
    selectedWard: Ward?,
    onWardSelected: (Ward) -> Unit,

    addressDetail: String,
    onAddressDetailChange: (String) -> Unit
) {
    var provinceExpanded by remember { mutableStateOf(false) }
    var wardExpanded by remember { mutableStateOf(false) }

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

        // ===== TỈNH =====
        ExposedDropdownMenuBox(
            expanded = provinceExpanded,
            onExpandedChange = { provinceExpanded = !provinceExpanded }
        ) {
            OutlinedTextField(
                value = selectedProvince?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Tỉnh / Thành phố *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = provinceExpanded,
                onDismissRequest = { provinceExpanded = false }
            ) {
                provinces.forEach { province ->
                    DropdownMenuItem(
                        text = { Text(province.name) },
                        onClick = {
                            onProvinceSelected(province)
                            provinceExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // ===== PHƯỜNG =====
        ExposedDropdownMenuBox(
            expanded = wardExpanded,
            onExpandedChange = { wardExpanded = !wardExpanded }
        ) {
            OutlinedTextField(
                value = selectedWard?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Phường / Xã *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = wardExpanded,
                onDismissRequest = { wardExpanded = false }
            ) {
                wards.forEach { ward ->
                    DropdownMenuItem(
                        text = { Text(ward.name) },
                        onClick = {
                            onWardSelected(ward)
                            wardExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        ProfileTextField(
            label = "Số nhà, tên đường *",
            value = addressDetail,
            onChange = onAddressDetailChange
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