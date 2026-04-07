package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.runtime.Composable

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appointmentschedulingapp.domain.model.User
import com.example.appointmentschedulingapp.ui.features.account.AccountScreen
import com.example.appointmentschedulingapp.ui.features.account.settingContent.PrivacyPolicyScreen
import com.example.appointmentschedulingapp.ui.features.account.settingContent.TermsOfServiceScreen
import com.example.appointmentschedulingapp.ui.features.account.settingContent.TermsOfUseScreen
import com.example.appointmentschedulingapp.ui.features.auth.AuthRoute
import com.example.appointmentschedulingapp.ui.features.auth.AuthViewModel
import com.example.appointmentschedulingapp.ui.features.auth.OtpRoute

import com.example.appointmentschedulingapp.ui.features.home.HomeScreen
import com.example.appointmentschedulingapp.ui.features.notifications.NotificationsScreen
import com.example.appointmentschedulingapp.ui.features.profile.ProfileScreen
import com.example.appointmentschedulingapp.ui.features.tickets.TicketsScreen

@Composable
fun AppNavGraph(navController: NavHostController,
                session: User,
                onLogout: () -> Unit) {

    val authViewModel: AuthViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,

    ) {

        bookingNavGraph(navController)

        // Màn hình Home đã có file riêng
        composable(Screen.Home.route) {
            HomeScreen(
                session = session,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }


        // Các màn hình khác tạm thời để Text để không bị lỗi Build
        composable(Screen.Ticket.route) {
            TicketsScreen()
        }

        composable(Screen.Account.route) {
            AccountScreen(
                isLoggedIn = session.isLoggedIn,
                phoneNumber = session.phoneNumber,
                onLogout = onLogout,

                onNavigateToTerms = {
                    navController.navigate(Screen.TermsOfUse.route)
                },
                onNavigateToPrivacy = {
                    navController.navigate(Screen.PrivatePolicy.route)
                },
                onNavigateToService = {
                    navController.navigate(Screen.TermOfService.route)
                },
                onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route)
                }
            )
        }

        // Trong AppNavGraph
        composable(Screen.Auth.route) {
            AuthRoute(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onNavigateOtp = { phone ->
                    navController.navigate("otp_verification/$phone")
                }
            )
        }

        composable(
            route = "otp_verification/{phoneNumber}",
            arguments = listOf(navArgument("phoneNumber") {
                type = NavType.StringType
            })
        ) {
            OtpRoute(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }

            )
        }

        composable(Screen.TermOfService.route) {
            TermsOfServiceScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.TermsOfUse.route) {
            // SỬA TẠI ĐÂY: Chỉ cần onBack, xóa cái onNavigateToTerms thừa đi
            TermsOfUseScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.PrivatePolicy.route) {
            PrivacyPolicyScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Notification.route) {
            NotificationsScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}