package com.jeremiascortes.flowguide.features.auth.domain.usecase

import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

/**
 * Caso de uso para registro
 */

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        birthday: String,
        password: String,
        confirmPassword: String
    ): AuthResult<Unit> {
        return repository.register(
            name = name,
            email = email,
            birthday = birthday,
            password = password,
            confirmPassword = confirmPassword
        )
    }
}