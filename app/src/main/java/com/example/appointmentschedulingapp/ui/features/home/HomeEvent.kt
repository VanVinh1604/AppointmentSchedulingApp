package com.example.appointmentschedulingapp.ui.features.home

sealed class HomeEvent {
    data class Navigate(val route: String) : HomeEvent()
}