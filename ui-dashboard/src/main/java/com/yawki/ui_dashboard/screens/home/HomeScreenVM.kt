package com.yawki.ui_dashboard.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.usecases.GetMonksUseCase
import com.yawki.common.presentation.SharedViewModel
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val composeNavigator: ComposeNavigator,
    private val getMonksUseCase: GetMonksUseCase,
) : ViewModel() {

    var homeScreenUIState by mutableStateOf(HomeScreenUIState())
        private set

    init {
        fetchMonks()
    }

    private fun fetchMonks() {
//        dashboardUiState = dashboardUiState.copy(loading = true)
        viewModelScope.launch {
            getMonksUseCase.invoke().catch {
                homeScreenUIState = homeScreenUIState.copy(
                    loading = false,
                    errorMessage = it.message
                )
            }.collect {
                homeScreenUIState = when (it) {
                    is SafeResult.Failure -> homeScreenUIState.copy(
                        loading = false,
                        errorMessage = it.message
                    )

                    is SafeResult.Success -> homeScreenUIState.copy(
                        loading = false,
                        monks = it.data
                    )

                    SafeResult.Loading -> homeScreenUIState.copy(loading = true)
                }
            }
        }
    }

    fun onEvent(event: HomeScreenUIEvent) {
        when (event) {
            HomeScreenUIEvent.FetchMonk -> fetchMonks()
            is HomeScreenUIEvent.OnMonkSelected -> {
                homeScreenUIState.copy(selectedMonk = event.selectedMonk)
                composeNavigator.navigate(YawKiScreens.AudioListUIScreen.route)
            }
        }
    }

}