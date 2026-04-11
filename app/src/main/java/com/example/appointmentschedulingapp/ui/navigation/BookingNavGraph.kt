package com.example.appointmentschedulingapp.ui.navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.example.appointmentschedulingapp.ui.features.booking.*
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep1Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep2Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep3Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep4Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingReceiptScreen


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

        // bookingNavGraph.kt

        // bookingNavGraph.kt

        // bookingNavGraph.kt

        composable(
            route = Screen.BookingStep4.route
            // ✅ Bỏ hết deepLinks và arguments
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }
            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            // ✅ Bỏ orderId và hasProcessedResult — không cần nữa
            // ✅ Bỏ LaunchedEffect(orderId) — MomoCallbackBus trong ViewModel xử lý

            // Khi isSuccess thay đổi, chuyển trang sang Receipt
            LaunchedEffect(uiState.isSuccess) {
                if (uiState.isSuccess) {  // ✅ Bỏ điều kiện orderId != null
                    android.util.Log.d("BookingNavGraph", "Payment verified! Navigating to receipt")
                    navController.navigate(Screen.BookingReceipt.route) {
                        popUpTo("booking_graph") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }

            BookingStep4Screen(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onPaymentSelected = { method -> bookingViewModel.onEvent(BookingEvent.SelectPaymentMethod(method)) },
                onConfirmPayment = { bookingViewModel.onEvent(BookingEvent.ConfirmBooking) },
                onOpenPaymentUrl = { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        setPackage("vn.momo.platform.test")
                    }
                    try {
                        navController.context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            navController.context,
                            "MoMo app chưa được cài đặt",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                onNavigateToReceipt = {
                    navController.navigate(Screen.BookingReceipt.route) {
                        popUpTo("booking_step1") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onVerifyPayment = { bookingId ->
                    bookingViewModel.onMomoPaymentReturned(bookingId)
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