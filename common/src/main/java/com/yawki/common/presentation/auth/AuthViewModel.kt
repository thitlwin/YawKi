package com.yawki.common.presentation.auth

import androidx.lifecycle.viewModelScope
import com.yawki.common.domain.getSuccessOrNull
import com.yawki.common.domain.succeeded
import com.yawki.common.domain.usecases.SignInUseCase
import com.yawki.common.domain.usecases.SignOutUseCase
import com.yawki.common.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase
) : BaseViewModel<AuthUiState, AuthUIEvent>(AuthUiState()) {

    private fun signInAnonymously() = viewModelScope.launch((Dispatchers.IO)) {
        val res = signInUseCase.invoke()
        if (res.getSuccessOrNull() != null) {
            val user = res.getSuccessOrNull()!!.user
            updateUiState {
                AuthUiState(
                    user = user,
                    isAnonymous = user?.isAnonymous ?: false,
                    isAuthenticated = user != null,
                    authState = if (user != null) {
                        if (user.isAnonymous) AuthState.Authenticated else AuthState.SignedIn
                    } else {
                        AuthState.SignedOut
                    }
                )
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = signOutUseCase.invoke()
            if (res.succeeded) {
                updateUiState {
                    AuthUiState(
                        user = null,
                        isAnonymous = false,
                        isAuthenticated = false,
                        authState = AuthState.SignedOut
                    )
                }
            }
        }
    }

    override suspend fun handleEvent(event: AuthUIEvent) {
        when (event) {
            AuthUIEvent.SignInAnonymously -> signInAnonymously()
            AuthUIEvent.SignOut -> signOut()
        }
    }
}