package com.yawki.common.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.repositories.AuthRepository
import com.yawki.common.domain.repositories.AuthStateResponse
import com.yawki.common.domain.repositories.FirebaseSignInResponse
import com.yawki.common.domain.repositories.SignOutResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse = callbackFlow {

        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser)
            Log.i("TAG", "User: ${auth.currentUser?.uid ?: "Not authenticated"}")
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), firebaseAuth.currentUser)

    override suspend fun signInAnonymously(): FirebaseSignInResponse {
        return try {
            val authResult = firebaseAuth.signInAnonymously().await()
            authResult?.user?.let { user ->
                Log.i("TAG", "FirebaseAuthSuccess: Anonymous UID: ${user.uid}")
            }
            SafeResult.Success(authResult)
        } catch (error: Exception) {
            Log.e("TAG", "FirebaseAuthError: Failed to Sign in anonymously")
            SafeResult.Error(error)
        }
    }

    override suspend fun signOut(): SignOutResponse {
        return try {
            firebaseAuth.signOut()
            SafeResult.Success(true)
        } catch (e: Exception) {
            SafeResult.Error(e)
        }
    }
}