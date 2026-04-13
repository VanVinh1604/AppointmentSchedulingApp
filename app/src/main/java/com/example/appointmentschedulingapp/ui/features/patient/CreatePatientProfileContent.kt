package com.example.appointmentschedulingapp.ui.features.patient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appointmentschedulingapp.ui.features.patient.components.DocumentSection
import com.example.appointmentschedulingapp.ui.features.patient.components.MedicalSection
import com.example.appointmentschedulingapp.ui.features.patient.components.PersonalInfoSection
import com.example.appointmentschedulingapp.ui.features.patient.components.QuickScanCard
import com.example.appointmentschedulingapp.ui.features.patient.components.RelationshipSection

@Composable
fun CreatePatientProfileContent(
    padding: PaddingValues,
    uiState: CreatePatientProfileUiState,
    onEvent: (CreatePatientProfileEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        QuickScanCard()

        PersonalInfoSection(
            fullName = uiState.fullName,
            onFullNameChange = {
                onEvent(CreatePatientProfileEvent.FullNameChanged(it))
            },
            dateOfBirth = uiState.dateOfBirth,
            onDateOfBirthChange = {
                onEvent(CreatePatientProfileEvent.DateOfBirthChanged(it))
            },
            gender = uiState.gender,
            onGenderChange = {
                onEvent(CreatePatientProfileEvent.GenderChanged(it))
            },
            phoneNumber = uiState.phoneNumber,
            onPhoneChange = {
                onEvent(CreatePatientProfileEvent.PhoneChanged(it))
            },

            provinces = uiState.provinces,
            selectedProvince = uiState.selectedProvince,
            onProvinceSelected = {
                onEvent(CreatePatientProfileEvent.ProvinceSelected(it))
            },

            wards = uiState.wards,
            selectedWard = uiState.selectedWard,
            onWardSelected = {
                onEvent(CreatePatientProfileEvent.WardSelected(it))
            },

            addressDetail = uiState.addressDetail,
            onAddressDetailChange = {
                onEvent(CreatePatientProfileEvent.AddressDetailChanged(it))
            }
        )

        DocumentSection(
            identityCard = uiState.identityCard,
            onIdentityCardChange = {
                onEvent(CreatePatientProfileEvent.IdentityCardChanged(it))
            },
            insuranceNumber = uiState.healthInsuranceNumber,
            onInsuranceChange = {
                onEvent(CreatePatientProfileEvent.InsuranceChanged(it))
            },
            insuranceExpiry = uiState.healthInsuranceExpiry,
            onExpiryChange = {
                onEvent(CreatePatientProfileEvent.InsuranceExpiryChanged(it))
            }
        )

        RelationshipSection(
            relationship = uiState.relationship,
            onRelationshipChange = {
                onEvent(CreatePatientProfileEvent.RelationshipChanged(it))
            },
            emergencyContact = uiState.emergencyContact,
            onEmergencyChange = {
                onEvent(CreatePatientProfileEvent.EmergencyContactChanged(it))
            }
        )

        MedicalSection(
            allergies = uiState.allergies,
            onAllergiesChange = {
                onEvent(CreatePatientProfileEvent.AllergiesChanged(it))
            },
            medicalHistory = uiState.medicalHistory,
            onHistoryChange = {
                onEvent(CreatePatientProfileEvent.MedicalHistoryChanged(it))
            }
        )
    }
}