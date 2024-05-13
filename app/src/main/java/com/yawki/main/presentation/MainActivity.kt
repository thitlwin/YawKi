package com.yawki.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiRoute
import com.yawki.ui_dashboard.nav.dashboardNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var composeNavigator: ComposeNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                composeNavigator.handleNavigationCommands(navController)
            }

            NavHost(
                navController = navController,
                startDestination = YawKiRoute.Dashboard.name
            ) {
                dashboardNavigation(
                    composeNavigator = composeNavigator
                )
            }
        }
    }
}