package com.yawki.ui_dashboard.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yawki.audiolist.AudioListUI
import com.yawki.common.presentation.SharedViewModel
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiRoute
import com.yawki.navigator.YawKiScreens
import com.yawki.player.PlayerUI
import com.yawki.ui_dashboard.compose.DashboardUI


fun NavGraphBuilder.dashboardNavigation(
    composeNavigator: ComposeNavigator,
    sharedViewModel: SharedViewModel,
) {
    navigation(
        startDestination = YawKiScreens.Dashboard.name,
        route = YawKiRoute.Dashboard.name
    ) {
        composable(YawKiScreens.Dashboard.name) {
            DashboardUI(
                composeNavigator,
                sharedViewModel = sharedViewModel
            )
        }

        composable(YawKiScreens.AudioListUIScreen.name) {
            AudioListUI(
                composeNavigator,
                sharedViewModel = sharedViewModel
            )
        }

        composable(YawKiScreens.PlayerUIScreen.name) {
            PlayerUI(composeNavigator,
                sharedViewModel = sharedViewModel)
        }
    }
}