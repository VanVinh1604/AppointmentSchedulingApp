package com.example.appointmentschedulingapp.ui.features.patient

import com.example.appointmentschedulingapp.domain.model.location.Province
import com.example.appointmentschedulingapp.domain.model.location.Ward

sealed interface CreatePatientProfileEvent {
    data class FullNameChanged(val value: String) : CreatePatientProfileEvent
    data class DateOfBirthChanged(val value: String) : CreatePatientProfileEvent
    data class GenderChanged(val value: String) : CreatePatientProfileEvent
    data class PhoneChanged(val value: String) : CreatePatientProfileEvent
    data class AddressDetailChanged(val value: String) : CreatePatientProfileEvent
    data class ProvinceSelected(val province: Province): CreatePatientProfileEvent
    data class WardSelected(val ward: Ward): CreatePatientProfileEvent
    data class IdentityCardChanged(val value: String) : CreatePatientProfileEvent
    data class InsuranceChanged(val value: String) : CreatePatientProfileEvent
    data class InsuranceExpiryChanged(val value: String) : CreatePatientProfileEvent

    data class RelationshipChanged(val value: String) : CreatePatientProfileEvent
    data class EmergencyContactChanged(val value: String) : CreatePatientProfileEvent

    data class AllergiesChanged(val value: String) : CreatePatientProfileEvent
    data class MedicalHistoryChanged(val value: String) : CreatePatientProfileEvent

    data class DefaultChanged(val value: Boolean) : CreatePatientProfileEvent

    data object SaveProfile : CreatePatientProfileEvent
}