package com.yawki.ui_dashboard.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.yawki.audiolist.AudioListUI
import com.yawki.common.presentation.SharedViewModel
import com.yawki.navigator.YawKiRoute
import com.yawki.navigator.YawKiScreens
import com.yawki.player.PlayerUI
import com.yawki.ui_dashboard.compose.DashboardUI


fun NavGraphBuilder.dashboardNavigation(
    sharedViewModel: SharedViewModel,
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
                sharedViewModel = sharedViewModel
            )
        }

        composable(YawKiScreens.PlayerUIScreen.name) {
            PlayerUI(sharedViewModel = sharedViewModel)
        }
    }
}