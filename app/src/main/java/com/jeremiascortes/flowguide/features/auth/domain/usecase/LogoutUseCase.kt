package com.jeremiascortes.flowguide.features.auth.domain.usecase

import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de uso para logout
 */

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): AuthResult<Unit> {
        return repository.logout()
    }
}