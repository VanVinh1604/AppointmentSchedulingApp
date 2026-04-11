package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingReceiptScreen
import com.example.appointmentschedulingapp.ui.features.tickets.TicketDetailScreen
import com.example.appointmentschedulingapp.ui.features.tickets.TicketsViewModel

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
                },
                onNavigateToReceipt = {
                    navController.navigate(Screen.BookingReceipt.route) {
                        popUpTo("booking_step1") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.BookingReceipt.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            BookingReceiptScreen(
                uiState = uiState,
                bookingId = uiState.bookingId,
                onBackHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                },
                onViewBooking = {
//                    navController.navigate(Screen.BookingHistory.route)
                }
            )
        }


    }
}