package com.yawki.ui_dashboard.screens.home

import com.yawki.common.domain.models.monk.Monk

data class HomeScreenUIState(
    val loading: Boolean? = false,
    val monks: List<Monk>? = emptyList(),
    val selectedMonk: Monk? = null,
    val errorMessage: String? = null
)