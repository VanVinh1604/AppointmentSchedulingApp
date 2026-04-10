package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.runtime.LaunchedEffect
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
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep3Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep4Screen

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

        composable(Screen.BookingStep2.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            val created =
                backStackEntry.savedStateHandle
                    .getStateFlow("patient_profile_created", false)
                    .collectAsState()

            LaunchedEffect(created.value) {
                if (created.value) {
                    bookingViewModel.loadPatientProfiles()
                    backStackEntry.savedStateHandle["patient_profile_created"] = false
                }
            }

            BookingStep2Screen(
                uiState = uiState,
                onLoadPatients = { bookingViewModel.loadPatientProfiles() },
                onPatientSelected = { id, name ->
                    bookingViewModel.onEvent(
                        BookingEvent.SelectPatient(id, name)
                    )
                    navController.navigate(Screen.BookingStep3.route)
                },
                onBack = { navController.popBackStack() },
                onCreatePatient = {
                    navController.navigate(Screen.CreatePatientProfile.route)
                }
            )
        }

        composable(Screen.BookingStep3.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            BookingStep3Screen(
                uiState = uiState,
                onBack = {
                    navController.popBackStack()
                },
                onConfirm = {
                    navController.navigate(Screen.BookingStep4.route)
                }
            )
        }

        composable(Screen.BookingStep4.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            BookingStep4Screen(
                uiState = uiState,
                onBack = {
                    navController.popBackStack()
                },
                onPaymentSelected = {
//                    paymentMethod ->
//                    bookingViewModel.onEvent(
//                        BookingEvent.SelectPaymentMethod(paymentMethod)
//                    )
                },
                onConfirmPayment = {
                    bookingViewModel.onEvent(BookingEvent.ConfirmBooking)
                }
            )
        }
    }
}