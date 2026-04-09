package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.appointmentschedulingapp.ui.features.booking.*
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep1Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep2Screen

fun NavGraphBuilder.bookingNavGraph(
    navController: NavHostController
) {
    navigation(
        route = "booking_graph",
        startDestination = Screen.SelectClinic.route
    ) {
        composable(Screen.SelectClinic.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

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
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            BookingStep1Screen(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onNext = {
                    navController.navigate("booking_step2")
                },
                onEvent = bookingViewModel::onEvent
            )
        }

        composable("booking_step2") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            BookingStep2Screen(
                uiState = uiState,
                patientList = listOf(

                ),
                onPatientSelected = { id, name ->
                    bookingViewModel.onEvent(
                        BookingEvent.SelectPatient(id, name)
                    )
                    navController.navigate("booking_step3")
                },
                onBack = { navController.popBackStack() },
                onCreatePatient = {
                    navController.navigate(Screen.CreatePatientProfile.route)
                }
            )
        }
    }
}