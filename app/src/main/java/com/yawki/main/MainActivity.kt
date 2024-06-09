package com.yawki.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.yawki.common.data.service.YawKiPlayerService
import com.yawki.common.domain.usecases.DestroyMediaControllerUseCase
import com.yawki.common.presentation.SharedViewModel
import com.yawki.common_ui.theme.YawKiTheme
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiRoute
import com.yawki.ui_dashboard.nav.dashboardNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var composeNavigator: ComposeNavigator
    private val sharedViewModel: SharedViewModel by viewModels()
    @Inject
    lateinit var destroyMediaControllerUseCase: DestroyMediaControllerUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                composeNavigator.handleNavigationCommands(navController)
            }
            YawKiTheme {
                NavHost(
                    navController = navController,
                    startDestination = YawKiRoute.Dashboard.name
                ) {
                    dashboardNavigation(
                        sharedViewModel = sharedViewModel,
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        sharedViewModel.destroyMediaController()
        destroyMediaControllerUseCase.invoke()
        stopService(Intent(this, YawKiPlayerService::class.java))
    }
}