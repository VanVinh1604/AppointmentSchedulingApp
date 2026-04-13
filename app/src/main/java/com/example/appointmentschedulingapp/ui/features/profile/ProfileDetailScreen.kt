package com.example.appointmentschedulingapp.ui.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appointmentschedulingapp.domain.enum.ProfileDetailField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    onBack: () -> Unit
) {
    val viewModel: ProfileDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val primaryColor = Color(0xFF1976D2)
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Pop back khi xóa thành công
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onBack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (uiState.isEditMode) "Chỉnh sửa hồ sơ" else "Chi tiết hồ sơ",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.isEditMode) viewModel.toggleEditMode()
                        else onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    if (uiState.isEditMode) {
                        IconButton(
                            onClick = { viewModel.saveProfile() },
                            enabled = !uiState.isSaving
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                        }
                    } else {
                        IconButton(onClick = { viewModel.toggleEditMode() }) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = primaryColor)
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            uiState.profile != null -> {
                ProfileDetailContent(
                    uiState = uiState,
                    padding = padding,
                    primaryColor = primaryColor,
                    onFieldChanged = viewModel::onFieldChanged
                )
            }
        }
    }

    // Dialog xác nhận xóa
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xóa hồ sơ") },
            text = { Text("Bạn có chắc muốn xóa hồ sơ \"${uiState.profile?.fullName}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteProfile()
                    }
                ) {
                    Text("Xóa", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
private fun ProfileDetailContent(
    uiState: ProfileDetailUiState,
    padding: PaddingValues,
    primaryColor: Color,
    onFieldChanged: (ProfileDetailField, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(Color(0xFFF7FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Header card profile
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = primaryColor),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = uiState.fullName.take(1).uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    uiState.fullName.ifBlank { "Hồ sơ bệnh nhân" },
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    uiState.relationship.ifBlank { "Bản thân" },
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        ProfileSection("Thông tin cơ bản") {
            ProfileField("Họ và tên", uiState.fullName, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.FULL_NAME, it)
            }
            ProfileField("Ngày sinh", uiState.dateOfBirth, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.DATE_OF_BIRTH, it)
            }
            ProfileField("Giới tính", uiState.gender, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.GENDER, it)
            }
            ProfileField("Số điện thoại", uiState.phoneNumber, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.PHONE, it)
            }
            ProfileField("Địa chỉ", uiState.address, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.ADDRESS, it)
            }
        }

        ProfileSection("Giấy tờ & bảo hiểm") {
            ProfileField("CMND/CCCD", uiState.identityCard, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.IDENTITY_CARD, it)
            }
            ProfileField("Số BHYT", uiState.healthInsuranceNumber, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.INSURANCE_NUMBER, it)
            }
            ProfileField("Hạn BHYT", uiState.healthInsuranceExpiry, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.INSURANCE_EXPIRY, it)
            }
        }

        ProfileSection("Thông tin y tế") {
            ProfileField("Liên hệ khẩn cấp", uiState.emergencyContact, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.EMERGENCY_CONTACT, it)
            }
            ProfileField("Dị ứng", uiState.allergies, uiState.isEditMode, primaryColor) {
                onFieldChanged(ProfileDetailField.ALLERGIES, it)
            }
            ProfileField(
                "Tiền sử bệnh",
                uiState.medicalHistory,
                uiState.isEditMode,
                primaryColor,
                singleLine = false
            ) {
                onFieldChanged(ProfileDetailField.MEDICAL_HISTORY, it)
            }
        }

        if (uiState.isSaving) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = primaryColor
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )

            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    isEditMode: Boolean,
    primaryColor: Color,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    if (isEditMode) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor,
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value.ifBlank { "Chưa cập nhật" },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(color = Color(0xFFF1F1F1))
        }
    }
}