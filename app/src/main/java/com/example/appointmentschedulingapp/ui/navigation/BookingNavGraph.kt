package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.appointmentschedulingapp.ui.features.booking.*
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep1Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep2Screen

fun NavGraphBuilder.bookingNavGraph(
    navController: NavHostController
) {
    composable(Screen.SelectClinic.route) { backStackEntry ->

        val bookingViewModel: BookingViewModel = hiltViewModel(backStackEntry)

        SelectClinicScreen(
            bookingViewModel = bookingViewModel,
            onBack = { navController.popBackStack() },
            onNavigateToDetail = { clinicId ->
                navController.navigate(Screen.ClinicDetail.createRoute(clinicId))
            },
            onNavigateToBookingStep1 = {
                navController.navigate(Screen.BookingStep1.route)
            }
        )
    }

    composable(Screen.BookingStep1.route) { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.SelectClinic.route)
        }

        val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
        val uiState by bookingViewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            bookingViewModel.onEvent(BookingEvent.SetStep(1))
        }

        BookingStep1Screen(
            uiState = uiState,
            onBack = { navController.popBackStack() },
            onNext = {
                bookingViewModel.onEvent(BookingEvent.SetStep(2))
                navController.navigate("booking_step2")
            },
            onUpdateSpecialty = {
                bookingViewModel.onEvent(
                    BookingEvent.UpdateSpecialty(it)
                )
            }
        )
    }

    composable("booking_step2") { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.SelectClinic.route)
        }

        val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
        val uiState by bookingViewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            bookingViewModel.onEvent(BookingEvent.SetStep(2))
        }

        BookingStep2Screen(
            uiState = uiState,
            onPatientSelected = { id, name ->
                bookingViewModel.onEvent(
                    BookingEvent.SelectPatient(id, name)
                )
            },
            onBack = { navController.popBackStack() },
            onNext = {}
        )
    }
}