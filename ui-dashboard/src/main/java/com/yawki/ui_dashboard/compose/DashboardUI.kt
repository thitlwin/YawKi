package com.yawki.ui_dashboard.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yawki.common.presentation.SharedViewModel
import com.yawki.common_ui.theme.YawKiTheme
import com.yawki.common_ui.theme.YawKiTypography
import com.yawki.navigator.ComposeNavigator
import com.yawki.ui_dashboard.R
import com.yawki.ui_dashboard.screens.home.HomeScreenUI
import com.yawki.ui_dashboard.screens.favorite.PlaylistScreenUI
import com.yawki.ui_dashboard.screens.setting.SettingScreenUI

@Composable
fun DashboardUI(
    sharedViewModel: SharedViewModel,
) {
    val scaffoldState = rememberScaffoldState()
    val dashboardNavController = rememberNavController()
    YawKiTheme {
        DashboardScreen(
            scaffoldState,
            dashboardNavController,
            sharedViewModel
        )
    }
}

@Composable
fun DashboardScreen(
    scaffoldState: ScaffoldState,
    dashboardNavController: NavHostController,
    sharedViewModel: SharedViewModel,
) {
    DashboardScaffold(
        scaffoldState = scaffoldState,
        dashboardNavController = dashboardNavController,
        sharedViewModel = sharedViewModel
    )
}

@Composable
private fun DashboardScaffold(
    scaffoldState: ScaffoldState,
    dashboardNavController: NavHostController,
    sharedViewModel: SharedViewModel,
) {
    Surface(contentColor = MaterialTheme.colorScheme.background) {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding(),
            scaffoldState = scaffoldState,
            bottomBar = {
                DashboardBottomNavBar(dashboardNavController)
            },
            snackbarHost = {
                scaffoldState.snackbarHostState
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    dashboardNavController,
                    startDestination = DashboardScreens.Home.route,
                ) {
                    composable(DashboardScreens.Home.route) {
                        HomeScreenUI(sharedViewModel)
                    }
                    composable(DashboardScreens.Setting.route) {
                        SettingScreenUI()
                    }
                    composable(DashboardScreens.Favorites.route) {
                        PlaylistScreenUI()
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardBottomNavBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val dashTabs = getDashTabs()
        dashTabs.forEachIndexed { _, screen ->
            BottomNavItem(screen, currentDestination, navController)
        }
    }
}

@Composable
fun RowScope.BottomNavItem(
    dashboardScreens: DashboardScreens,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        selectedContentColor = MaterialTheme.colorScheme.onPrimary,
//        unselectedContentColor = MaterialTheme.colorScheme.onSecondary,
        icon = { Icon(dashboardScreens.image, contentDescription = null) },
        label = {
            Text(
                stringResource(dashboardScreens.resourceId),
                maxLines = 1,
                style = YawKiTypography.bodySmall
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == dashboardScreens.route } == true,
        onClick = {
            navigateTab(navController, dashboardScreens)
        }
    )
}

fun navigateTab(navController: NavHostController, dashboardScreens: DashboardScreens) {
    navController.navigate(dashboardScreens.route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

sealed class DashboardScreens(
    val route: String,
    val image: ImageVector,
    @StringRes val resourceId: Int
) {
    data object Home : DashboardScreens("Home", Icons.Filled.Home, R.string.home)
    data object Favorites : DashboardScreens("Favorite", Icons.Filled.Favorite, R.string.favorite)
    data object Setting : DashboardScreens("Setting", Icons.Filled.Settings, R.string.setting)
}

fun getDashTabs(): MutableList<DashboardScreens> {
    return mutableListOf(
        DashboardScreens.Home, DashboardScreens.Favorites,
        DashboardScreens.Setting
    )
}
