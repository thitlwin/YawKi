package com.yawki.ui_dashboard.screens.home

import com.yawki.common.domain.models.monk.Monk

sealed class HomeScreenUIEvent {
    data object FetchMonk: HomeScreenUIEvent()
    data class OnMonkSelected(val selectedMonk: Monk): HomeScreenUIEvent()
}