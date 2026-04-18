package com.example.appointmentschedulingapp.ui.components.login

data class LoginRequirementState(
    val showDialog: Boolean = false,
    val onDismiss: () -> Unit = {},
    val onConfirm: () -> Unit = {}
)