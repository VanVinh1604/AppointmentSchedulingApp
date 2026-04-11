package com.example.appointmentschedulingapp.ui.features.tickets

import com.example.appointmentschedulingapp.domain.model.Booking

data class TicketsUiState(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val filteredBookings: List<Booking> = emptyList(),
    val selectedFilter: TicketFilter = TicketFilter.ALL,
    val errorMessage: String? = null,
    val selectedBooking: Booking? = null
)
