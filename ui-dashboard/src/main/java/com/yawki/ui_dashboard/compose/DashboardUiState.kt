package com.yawki.ui_dashboard.compose

import com.yawki.common.domain.models.monk.Monk

data class DashboardUiState(
    val loading: Boolean? = false,
    val errorMessage: String? = null
)