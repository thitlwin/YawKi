package com.yawki.ui_dashboard.compose

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yawki.navigator.ComposeNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val composeNavigator: ComposeNavigator,
) : ViewModel() {
}