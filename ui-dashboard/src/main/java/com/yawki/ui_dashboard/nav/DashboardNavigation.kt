package com.yawki.ui_dashboard.nav

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.yawki.audiolist.AudioListUI
import com.yawki.common.presentation.SharedViewModel
import com.yawki.common.presentation.auth.AuthViewModel
import com.yawki.common_ui.auth.YawKiAuthUI
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiRoute
import com.yawki.navigator.YawKiScreens
import com.yawki.player.PlayerUI
import com.yawki.ui_dashboard.compose.DashboardUI


fun NavGraphBuilder.dashboardNavigation(
    sharedViewModel: SharedViewModel,
    composeNavigator: ComposeNavigator,
) {
    navigation(
        startDestination = YawKiScreens.Dashboard.name,
        route = YawKiRoute.Dashboard.name
    ) {
        composable(YawKiScreens.Dashboard.name) {
            DashboardUI(
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = YawKiScreens.AudioListUIScreen.name,
            arguments = listOf(navArgument("selectedMonkId") { type = NavType.IntType })
        ) {
            AudioListUI(
                sharedViewModel = sharedViewModel,
                composeNavigator = composeNavigator
            )
        }

        composable(YawKiScreens.PlayerUIScreen.name) {
            PlayerUI(sharedViewModel = sharedViewModel)
        }

        composable(route = YawKiScreens.AuthUIScreen.name) {
            val authViewModel: AuthViewModel = hiltViewModel()
            YawKiAuthUI(authViewModel = authViewModel)
        }
    }
}