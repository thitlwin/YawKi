package com.yawki.common.domain.usecases

import com.yawki.common.domain.repositories.AuthRepository
import com.yawki.common.domain.repositories.FirebaseSignInResponse
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): FirebaseSignInResponse {
        return authRepository.signInAnonymously()
    }
}