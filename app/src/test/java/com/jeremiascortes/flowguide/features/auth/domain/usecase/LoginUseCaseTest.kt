package com.jeremiascortes.flowguide.features.auth.domain.usecase

import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    companion object {
        // Correo de ejemplo
        private const val TEST_EMAIL = "test@example.com"

        // Contraseña segura de ejemplo
        private const val TEST_SAVE_PASSWORD = "password123*"

        // Contraseña incorrecta de ejemplo
        private const val TEST_WRONG_PASSWORD = "wrongpassword"

        // User-ID de ejemplo
        private const val TEST_USER_ID = "user-123"
    }

    // Mock del repositorio
    private val repository: AuthRepository = mockk()

    // UseCase a probar
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        loginUseCase = LoginUseCase(repository)
    }

    // estado inicial → éxito → error → interacción
    //  'qué se prueba' + 'bajo qué condición' + 'resultado esperado'

    @Test
    fun `Login por primera vez, se espera un resultado exitoso`() = runTest {
        coEvery {
            repository.login(
                TEST_EMAIL,
                TEST_SAVE_PASSWORD
            )
        } returns AuthResult.Success(Unit)

        val result = loginUseCase(TEST_EMAIL, TEST_SAVE_PASSWORD)

        assertTrue(
            "El resultado debió ser Success, pero fue $result",
            result is AuthResult.Success
        )
    }
    
    @Test
    fun `Login por primera vez, se espera un resultado de error`() = runTest {
        coEvery {
            repository.login(
                TEST_EMAIL,
                TEST_WRONG_PASSWORD
            )
        } returns AuthResult.Error("Hubo un error")

        val result = loginUseCase(TEST_EMAIL, TEST_WRONG_PASSWORD)

        assertTrue(
            "El resultado debió ser Error, pero fue $result",
            result is AuthResult.Error
        )
    }
}