package com.yawki.common.domain.usecases

import com.yawki.common.domain.repositories.AuthRepository
import com.yawki.common.domain.repositories.SignOutResponse
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): SignOutResponse {
        return authRepository.signOut()
    }
}