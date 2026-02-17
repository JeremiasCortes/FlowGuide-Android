package com.jeremiascortes.flowguide.features.auth.domain.usecase

/**
 * ============================================================================
 * CAPA: Domain - Use Case
 * ============================================================================
 *
 * Caso de uso para iniciar sesión con email y contraseña.
 *
 * PATRÓN USE CASE:
 * - Cada caso de uso tiene UNA responsabilidad única
 * - Se inyecta en el ViewModel
 * - encapsula la lógica de negocio
 *
 * POR QUÉ USE CASES Y NO LLAMAR DIRECTAMENTE AL REPOSITORIO:
 * - Separación de responsabilidades
 * - Reutilización: el mismo caso de uso puede usarse en varios ViewModels
 * - Testabilidad: fácil de hacer unit tests
 * - Escalabilidad: si la lógica se complica, está en un solo lugar
 *
 * EJEMPLO DE USO EN VIEWMODEL:
 * ```kotlin
 * fun login(email: String, password: String) {
 *     viewModelScope.launch {
 *         _authResult.value = loginUseCase(email, password)
 *     }
 * }
 * ```
 * ============================================================================
 */

import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Ejecuta el caso de uso.
     * El operador invoke permite llamarlo como una función: loginUseCase(email, password)
     */
    suspend operator fun invoke(
        email: String,
        password: String
    ): AuthResult<Unit> {
        return repository.login(
            email = email,
            password = password
        )
    }
}
