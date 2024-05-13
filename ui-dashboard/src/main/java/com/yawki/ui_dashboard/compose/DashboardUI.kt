package com.yawki.ui_dashboard.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yawki.common_ui.theme.YawKiTheme
import com.yawki.common_ui.theme.YawkiTypography
import com.yawki.navigator.ComposeNavigator
import com.yawki.ui_dashboard.R
import com.yawki.ui_dashboard.screens.HomeScreenUI
import com.yawki.ui_dashboard.screens.PlaylistScreenUI
import com.yawki.ui_dashboard.screens.SettingScreenUI

@Composable
fun DashboardUI(
    composeNavigator: ComposeNavigator,
    dashboardVM: DashboardVM = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val dashboardNavController = rememberNavController()
    YawKiTheme {
        DashboardScreenRegular(
            scaffoldState,
            dashboardNavController,
            composeNavigator,
            dashboardVM
        )
    }
}

@Composable
fun DashboardScreenRegular(
    scaffoldState: ScaffoldState,
    dashboardNavController: NavHostController,
    composeNavigator: ComposeNavigator,
    dashboardVM: DashboardVM
) {
    DashboardScaffold(
        scaffoldState = scaffoldState,
        dashboardNavController = dashboardNavController,
        modifier = Modifier,
        appBarIconClick = { },
        composeNavigator = composeNavigator
    )
}

@Composable
private fun DashboardScaffold(
    scaffoldState: ScaffoldState,
    dashboardNavController: NavHostController,
    modifier: Modifier,
    appBarIconClick: () -> Unit,
    composeNavigator: ComposeNavigator,
) {
    Box(modifier) {
        Scaffold(
//            backgroundColor = SlackCloneColorProvider.colors.uiBackground,
//            contentColor = SlackCloneColorProvider.colors.textSecondary,
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
                    startDestination = Screen.Home.route,
                ) {
                    composable(Screen.Home.route) {
                        HomeScreenUI()
                    }
                    composable(Screen.Setting.route) {
                        SettingScreenUI()
                    }
                    composable(Screen.Playlist.route) {
                        PlaylistScreenUI()
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardBottomNavBar(navController: NavHostController) {
    Column(Modifier.background(color = MaterialTheme.colors.background)) {
        Divider(
            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.2f),
            thickness = 0.5.dp
        )
        BottomNavigation(backgroundColor = MaterialTheme.colors.background) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val dashTabs = getDashTabs()
            dashTabs.forEach { screen ->
                BottomNavItem(screen, currentDestination, navController)
            }
        }
    }
}

@Composable
fun RowScope.BottomNavItem(
    screen: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        selectedContentColor = MaterialTheme.colors.onPrimary,
        unselectedContentColor = MaterialTheme.colors.onSecondary,
        icon = { Icon(screen.image, contentDescription = null) },
        label = {
            Text(
                stringResource(screen.resourceId),
                maxLines = 1,
                style = YawkiTypography.headlineMedium
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navigateTab(navController, screen)
        }
    )
}

fun navigateTab(navController: NavHostController, screen: Screen) {
    navController.navigate(screen.route) {
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

sealed class Screen(
    val route: String,
    val image: ImageVector,
    @StringRes val resourceId: Int
) {
    object Home : Screen("Home", Icons.Filled.Home, R.string.home)
    object Playlist : Screen("Playlist", Icons.Filled.Home, R.string.playlist)
    object Setting : Screen("Setting", Icons.Filled.Home, R.string.setting)
}

fun getDashTabs(): MutableList<Screen> {
    return mutableListOf(
        Screen.Home, Screen.Playlist,
        Screen.Setting
    )
}
