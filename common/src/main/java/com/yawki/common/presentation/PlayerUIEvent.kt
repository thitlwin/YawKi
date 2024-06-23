package com.yawki.common.presentation

sealed class PlayerUIEvent {

    data object OnBackPress : PlayerUIEvent()
    data object OnDownload : PlayerUIEvent()
//    data object BindInitialState : PlayerUIEvent()
//    data object OpenFullScreenPlayer : PlayerUIEvent()

}