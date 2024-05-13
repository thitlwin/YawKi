package com.yawki.ui_dashboard.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiRoute
import com.yawki.navigator.YawKiScreens
import com.yawki.ui_dashboard.compose.DashboardUI


fun NavGraphBuilder.dashboardNavigation(
    composeNavigator: ComposeNavigator,
) {
    navigation(
        startDestination = YawKiScreens.Dashboard.name,
        route = YawKiRoute.Dashboard.name
    ) {
        composable(YawKiScreens.Dashboard.name) {
            DashboardUI(composeNavigator, )
        }
    }
}