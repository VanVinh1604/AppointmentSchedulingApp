package com.example.appointmentschedulingapp.ui.features.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.PatientProfile
import com.example.appointmentschedulingapp.domain.usecase.patientUsecase.CreatePatientProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePatientProfileViewModel @Inject constructor(
    private val createPatientProfileUseCase: CreatePatientProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePatientProfileUiState())
    val uiState = _uiState.asStateFlow()

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

            is CreatePatientProfileEvent.AddressChanged ->
                updateState { copy(address = event.value) }

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
                state.phoneNumber.isNotBlank()
    }
}