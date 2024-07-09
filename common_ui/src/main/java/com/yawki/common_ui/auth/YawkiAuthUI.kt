package com.yawki.common_ui.auth

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.yawki.common.presentation.auth.AuthUIEvent
import com.yawki.common.presentation.auth.AuthViewModel
import com.yawki.common_ui.theme.YawKiTheme

@Composable
fun YawKiAuthUI(
    authViewModel: AuthViewModel
) {
    AuthUiScreen { result ->

        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            user?.isAnonymous
            authViewModel.onEvent(AuthUIEvent.SignInAnonymously)
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }

//         (4) Handle the sign-in result callback
        if (result.resultCode == RESULT_OK) {
            authViewModel.onEvent(AuthUIEvent.SignInAnonymously)
        } else {
            val response = result.idpResponse
            if (response == null) {
//                viewModel.onSignInCancel()
            } else {
                val errorCode = response.error?.errorCode
//                viewModel.onSignInError(errorCode)
            }
        }
//        showSignIn = false
    }

}

@Composable
fun AuthUiScreen(onSignInResult: (FirebaseAuthUIAuthenticationResult) -> Unit) {

    // (1) Create ActivityResultLauncher
    val launcher = rememberLauncherForActivityResult(
        contract = FirebaseAuthUIActivityResultContract(),
        onResult = onSignInResult
    )

    // (2) Choose authentication providers
    val providers = arrayListOf(
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build(),
    )

    // (3) Create and launch sign-in intent
    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    YawKiTheme {
        LaunchedEffect(true) {
            launcher.launch(signInIntent)
        }
    }
}