package com.example.appointmentschedulingapp.domain.model

data class User(
    val id: String = "",
    val phoneNumber: String = "",
    val languagePreference: String = "vi",
    val isLoggedIn: Boolean = false

)