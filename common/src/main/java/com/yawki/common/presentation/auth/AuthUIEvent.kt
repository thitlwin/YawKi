package com.yawki.common.presentation.auth

sealed class AuthUIEvent {
    data object SignOut: AuthUIEvent()
    data object SignInAnonymously: AuthUIEvent()
}