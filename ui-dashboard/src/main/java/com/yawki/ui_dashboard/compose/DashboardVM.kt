package com.yawki.ui_dashboard.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.usecases.GetMonksUseCase
import com.yawki.navigator.ComposeNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val composeNavigator: ComposeNavigator,
) : ViewModel() {
    var dashboardUiState by mutableStateOf(DashboardUiState())
        private set


    fun onEvent(event: DashboardUiEvent) {
//        when (event) {
//            DashboardUiEvent.FetchMonk -> fetchMonks()
//            is DashboardUiEvent.OnMonSelected -> dashboardUiState.copy(selectedMonk = event.selectedMonk)
//        }
    }
}