package com.example.appointmentschedulingapp.ui.features.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.enum.ProfileDetailField
import com.example.appointmentschedulingapp.domain.model.PatientProfile
import com.example.appointmentschedulingapp.domain.usecase.patientUsecase.DeletePatientProfileUseCase
import com.example.appointmentschedulingapp.domain.usecase.patientUsecase.GetPatientProfileByIdUseCase
import com.example.appointmentschedulingapp.domain.usecase.patientUsecase.UpdatePatientProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPatientProfileByIdUseCase: GetPatientProfileByIdUseCase,
    private val updatePatientProfileUseCase: UpdatePatientProfileUseCase,
    private val deletePatientProfileUseCase: DeletePatientProfileUseCase
) : ViewModel() {

    private val profileId: String = checkNotNull(savedStateHandle["profileId"])

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState = _uiState.asStateFlow()

    init { loadProfile() }

    fun loadProfile() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        getPatientProfileByIdUseCase(profileId)
            .onSuccess { profile ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    profile = profile,
                    // Điền sẵn edit fields từ profile
                    fullName = profile.fullName,
                    dateOfBirth = profile.dateOfBirth,
                    gender = profile.gender,
                    phoneNumber = profile.phoneNumber,
                    address = profile.address,
                    identityCard = profile.identityCard,
                    healthInsuranceNumber = profile.healthInsuranceNumber,
                    healthInsuranceExpiry = profile.healthInsuranceExpiry,
                    relationship = profile.relationship,
                    emergencyContact = profile.emergencyContact,
                    allergies = profile.allergies,
                    medicalHistory = profile.medicalHistory,
                    isDefault = profile.isDefault
                )
            }
            .onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Không tải được hồ sơ"
                )
            }
    }

    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(
            isEditMode = !_uiState.value.isEditMode
        )
    }

    fun onFieldChanged(field: ProfileDetailField, value: String) {
        _uiState.value = when (field) {
            ProfileDetailField.FULL_NAME -> _uiState.value.copy(fullName = value)
            ProfileDetailField.DATE_OF_BIRTH -> _uiState.value.copy(dateOfBirth = value)
            ProfileDetailField.GENDER -> _uiState.value.copy(gender = value)
            ProfileDetailField.PHONE -> _uiState.value.copy(phoneNumber = value)
            ProfileDetailField.ADDRESS -> _uiState.value.copy(address = value)
            ProfileDetailField.IDENTITY_CARD -> _uiState.value.copy(identityCard = value)
            ProfileDetailField.INSURANCE_NUMBER -> _uiState.value.copy(healthInsuranceNumber = value)
            ProfileDetailField.INSURANCE_EXPIRY -> _uiState.value.copy(healthInsuranceExpiry = value)
            ProfileDetailField.RELATIONSHIP -> _uiState.value.copy(relationship = value)
            ProfileDetailField.EMERGENCY_CONTACT -> _uiState.value.copy(emergencyContact = value)
            ProfileDetailField.ALLERGIES -> _uiState.value.copy(allergies = value)
            ProfileDetailField.MEDICAL_HISTORY -> _uiState.value.copy(medicalHistory = value)
        }
    }

    fun saveProfile() = viewModelScope.launch {
        val state = _uiState.value
        val original = state.profile ?: return@launch

        _uiState.value = state.copy(isSaving = true, error = null)

        val updated = original.copy(
            fullName = state.fullName,
            dateOfBirth = state.dateOfBirth,
            gender = state.gender,
            phoneNumber = state.phoneNumber,
            address = state.address,
            identityCard = state.identityCard,
            healthInsuranceNumber = state.healthInsuranceNumber,
            healthInsuranceExpiry = state.healthInsuranceExpiry,
            relationship = state.relationship,
            emergencyContact = state.emergencyContact,
            allergies = state.allergies,
            medicalHistory = state.medicalHistory,
            isDefault = state.isDefault
        )

        updatePatientProfileUseCase(updated)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    isEditMode = false,
                    profile = updated
                )
            }
            .onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Cập nhật thất bại"
                )
            }
    }

    fun deleteProfile() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isDeleting = true, error = null)

        deletePatientProfileUseCase(profileId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    isSuccess = true // navigation sẽ lắng nghe cái này để pop back
                )
            }
            .onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    error = e.message ?: "Xóa thất bại"
                )
            }
    }
}

