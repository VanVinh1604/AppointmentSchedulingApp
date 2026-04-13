package com.example.appointmentschedulingapp.ui.features.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.PatientProfile
import com.example.appointmentschedulingapp.domain.model.location.Province
import com.example.appointmentschedulingapp.domain.usecase.location.LocationUseCases
import com.example.appointmentschedulingapp.domain.usecase.patientUsecase.CreatePatientProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePatientProfileViewModel @Inject constructor(
    private val createPatientProfileUseCase: CreatePatientProfileUseCase,
    private val locationUseCases: LocationUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePatientProfileUiState())
    val uiState = _uiState.asStateFlow()


    init {
        loadProvinces()
    }
    fun onEvent(event: CreatePatientProfileEvent) {
        when (event) {
            is CreatePatientProfileEvent.FullNameChanged ->
                updateState { copy(fullName = event.value) }

            is CreatePatientProfileEvent.DateOfBirthChanged ->
                updateState { copy(dateOfBirth = event.value) }

            is CreatePatientProfileEvent.GenderChanged ->
                updateState { copy(gender = event.value) }

            is CreatePatientProfileEvent.PhoneChanged ->
                updateState { copy(phoneNumber = event.value) }

            is CreatePatientProfileEvent.AddressDetailChanged ->
                updateState { copy(addressDetail = event.value) }

            is CreatePatientProfileEvent.ProvinceSelected ->
                onProvinceSelected(event.province)

            is CreatePatientProfileEvent.WardSelected ->
                updateState { copy(selectedWard = event.ward) }

            is CreatePatientProfileEvent.IdentityCardChanged ->
                updateState { copy(identityCard = event.value) }

            is CreatePatientProfileEvent.InsuranceChanged ->
                updateState { copy(healthInsuranceNumber = event.value) }

            is CreatePatientProfileEvent.InsuranceExpiryChanged ->
                updateState { copy(healthInsuranceExpiry = event.value) }

            is CreatePatientProfileEvent.RelationshipChanged ->
                updateState { copy(relationship = event.value) }

            is CreatePatientProfileEvent.EmergencyContactChanged ->
                updateState { copy(emergencyContact = event.value) }

            is CreatePatientProfileEvent.AllergiesChanged ->
                updateState { copy(allergies = event.value) }

            is CreatePatientProfileEvent.MedicalHistoryChanged ->
                updateState { copy(medicalHistory = event.value) }

            is CreatePatientProfileEvent.DefaultChanged ->
                updateState { copy(isDefault = event.value) }

            CreatePatientProfileEvent.SaveProfile -> saveProfile()
        }
    }

    private fun saveProfile() = viewModelScope.launch {
        val state = _uiState.value

        if (!isFormValid(state)) {
            updateState {
                copy(error = "Vui lòng nhập đầy đủ thông tin bắt buộc")
            }
            return@launch
        }

        updateState { copy(isLoading = true, error = null) }

        val profile = PatientProfile(
            fullName = state.fullName.trim(),
            dateOfBirth = state.dateOfBirth.trim(),
            gender = state.gender.trim(),
            phoneNumber = state.phoneNumber.trim(),

            provinceCode = state.selectedProvince?.code ?: 0,
            provinceName = state.selectedProvince?.name ?: "",

            wardCode = state.selectedWard?.code ?: 0,
            wardName = state.selectedWard?.name ?: "",

            addressDetail = state.addressDetail.trim(),

            identityCard = state.identityCard.trim(),
            healthInsuranceNumber = state.healthInsuranceNumber.trim(),
            healthInsuranceExpiry = state.healthInsuranceExpiry.trim(),

            relationship = state.relationship.trim(),
            emergencyContact = state.emergencyContact.trim(),

            allergies = state.allergies.trim(),
            medicalHistory = state.medicalHistory.trim(),

            isDefault = state.isDefault
        )

        createPatientProfileUseCase(profile)
            .onSuccess {
                updateState { copy(isLoading = false, isSuccess = true) }
            }
            .onFailure {
                updateState {
                    copy(
                        isLoading = false,
                        error = it.message ?: "Tạo hồ sơ thất bại"
                    )
                }
            }
    }

    private inline fun updateState(
        block: CreatePatientProfileUiState.() -> CreatePatientProfileUiState
    ) {
        _uiState.value = _uiState.value.block()
    }

    private fun isFormValid(state: CreatePatientProfileUiState): Boolean {
        return state.fullName.isNotBlank() &&
                state.dateOfBirth.isNotBlank() &&
                state.gender.isNotBlank() &&
                state.phoneNumber.length >= 10 &&
                state.selectedProvince != null &&
                state.selectedWard != null &&
                state.addressDetail.isNotBlank()
    }
    private fun loadProvinces() = viewModelScope.launch {
        updateState { copy(isLoadingLocation = true) }

        locationUseCases.getProvinces()
            .onSuccess { provinces ->
                updateState {
                    copy(
                        provinces = provinces,
                        isLoadingLocation = false
                    )
                }
            }
            .onFailure {
                updateState {
                    copy(
                        isLoadingLocation = false,
                        error = "Không tải được tỉnh thành"
                    )
                }
            }
    }

    private fun onProvinceSelected(province: Province) {
        updateState {
            copy(
                selectedProvince = province,
                selectedWard = null,
                wards = emptyList()
            )
        }

        viewModelScope.launch {
            locationUseCases.getWards(province.code)
                .onSuccess { wards ->
                    updateState { copy(wards = wards) }
                }
                .onFailure {
                    updateState {
                        copy(error = "Không tải được danh sách phường/xã")
                    }
                }
        }
    }
}