package com.example.appointmentschedulingapp.ui.features.home

import com.example.appointmentschedulingapp.domain.model.HomeAction

data class HomeUiState(
    val actions: List<HomeAction> = emptyList(),
    val isLoading: Boolean = false
)