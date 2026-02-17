package com.jeremiascortes.flowguide.features.auth.domain.usecase

import com.jeremiascortes.flowguide.features.auth.domain.model.AuthState
import com.jeremiascortes.flowguide.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

/**
 * Caso de uso para obtener sesión actual
 */

class GetCurrentSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): AuthState {
        return repository.getCurrentSession()
    }
}