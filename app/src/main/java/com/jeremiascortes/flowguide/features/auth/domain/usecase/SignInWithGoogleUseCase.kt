package com.jeremiascortes.flowguide.features.auth.domain.usecase

import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

/**
 * Caso de uso para autenticación con Google.
 * Funciona tanto para login como para registro (si el usuario no existe, se crea automáticamente).
 */
class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): AuthResult<Unit> {
        return repository.signInWithGoogle()
    }
}
