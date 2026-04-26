package com.jeremiascortes.flowguide.features.auth.presentation

import app.cash.turbine.test
import com.jeremiascortes.flowguide.MainDispatcherRule
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthState
import com.jeremiascortes.flowguide.features.auth.domain.usecase.GetCurrentSessionUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.LoginUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.LogoutUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.RegisterUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.SignInWithGoogleUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class AuthViewModelTest {

    /**
     * Reemplaza al `Dispatcher.Main` del hilo principal de Android.
     * Además, tiene que ser @get:Rule (no @Before) para ejecutarse antes del constructor
     * del ViewModel
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase: LoginUseCase = mockk()
    private val registerUseCase: RegisterUseCase = mockk()
    private val logoutUseCase: LogoutUseCase = mockk()
    private val getCurrentSessionUseCase: GetCurrentSessionUseCase = mockk(relaxed = true)
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase = mockk()

    private lateinit var viewModel: AuthViewModel

    companion object {
        const val TEST_NAME = "Test User"
        const val TEST_EMAIL = "test@example.com"
        const val TETS_BIRTHDATE = "1990-01-01"
        const val TEST_PASSWORD = "testpassword"
        const val TEST_CONFIRM_PASSWORD = "testpassword"

    }

    /**
     * Método para inicializar el ViewModel antes de cada prueba.
     */
    @Before
    fun setup() {

        viewModel = AuthViewModel(
            loginUseCase = loginUseCase,
            registerUseCase = registerUseCase,
            logoutUseCase = logoutUseCase,
            getCurrentSessionUseCase = getCurrentSessionUseCase,
            signInWithGoogleUseCase = signInWithGoogleUseCase
        )
    }

    @Test
    fun `Primera vez que se registra un usuario, lo hace bien y sale exitoso el registro`() =
        runTest {
            coEvery {
                registerUseCase(
                    TEST_NAME,
                    TEST_EMAIL,
                    TETS_BIRTHDATE,
                    TEST_PASSWORD,
                    TEST_CONFIRM_PASSWORD
                )
            }.returns(AuthResult.Success(Unit))

            viewModel.register(
                TEST_NAME,
                TEST_EMAIL,
                TETS_BIRTHDATE,
                TEST_PASSWORD,
                TEST_CONFIRM_PASSWORD
            )

            assertTrue(viewModel.authResult.value is AuthResult.Success)
        }

    @Test
    fun `El usuario se autentica correctamente`() = runTest {
        coEvery {
            loginUseCase(
                TEST_EMAIL,
                TEST_PASSWORD
            )
        }.returns(AuthResult.Success(Unit))

        viewModel.login(TEST_EMAIL, TEST_PASSWORD)

        assertTrue(viewModel.authResult.value is AuthResult.Success)
    }

    @Test
    fun `El usuario cierra sesion correctamente`() = runTest {
        coEvery { logoutUseCase() }.returns(AuthResult.Success(Unit))

        viewModel.logout()

        assertTrue(viewModel.authResult.value is AuthResult.Success)
    }

    @Test
    fun `El usuario se registra correctamente con Google`() = runTest {
        coEvery { signInWithGoogleUseCase() }.returns(AuthResult.Success(Unit))

        viewModel.signInWithGoogle()

        assertTrue(viewModel.authResult.value is AuthResult.Success)
    }

    @Test
    fun `El usuario se autentica correctamente con Google`() = runTest {
        coEvery { signInWithGoogleUseCase() }.returns(AuthResult.Success(Unit))

        viewModel.signInWithGoogle()

        assertTrue(viewModel.authResult.value is AuthResult.Success)
    }

    @Test
    fun `El usuario entra en la app y ya tenia session previa, se autentica correctamente`() =
        runTest {
            coEvery { getCurrentSessionUseCase() }.returns(AuthState.Authenticated("user-123"))

            assertTrue { viewModel.authState.value is AuthState.Authenticated }
        }

    @Test
    fun `registro existoso emite Loading, luego Success, y navega a Home`() = runTest {
        coEvery {
            registerUseCase(
                TEST_NAME,
                TEST_EMAIL,
                TETS_BIRTHDATE,
                TEST_PASSWORD,
                TEST_CONFIRM_PASSWORD
            )
        }.returns(AuthResult.Success(Unit))

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.register(
                TEST_NAME,
                TEST_EMAIL,
                TETS_BIRTHDATE,
                TEST_PASSWORD,
                TEST_CONFIRM_PASSWORD
            )

            assertEquals(AuthResult.Success(Unit), awaitItem())
        }
    }

    @Test
    fun `login exitoso emite Loading, luego Success, y navega a Home`() = runTest {
        coEvery { loginUseCase(TEST_EMAIL, TEST_PASSWORD) }
            .returns(AuthResult.Success(Unit))

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.login(TEST_EMAIL, TEST_PASSWORD)

            assertEquals(AuthResult.Success(Unit), awaitItem())

            ensureAllEventsConsumed()
        }

        viewModel.navigationEvent.test {
            assertEquals(NavigationEvent.ToHome, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `cierre de sesion exitoso emite Loading, luego Success, y navega a Login`() = runTest {
        coEvery { logoutUseCase() }.returns(AuthResult.Success(Unit))

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.logout()

            assertEquals(AuthResult.Success(Unit), awaitItem())
        }
    }

    @Test
    fun `registro fallido emite Loading, luego Error, y no navega`() = runTest {
        coEvery {
            registerUseCase(
                TEST_NAME,
                TEST_EMAIL,
                TETS_BIRTHDATE,
                TEST_PASSWORD,
                TEST_CONFIRM_PASSWORD
            )
        }.returns(AuthResult.Error("Error al registrar"))

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.register(
                TEST_NAME,
                TEST_EMAIL,
                TETS_BIRTHDATE,
                TEST_PASSWORD,
                TEST_CONFIRM_PASSWORD
            )

            assertEquals(AuthResult.Error("Error al registrar"), awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `login fallido emite Loading, luego Error, y no navega`() = runTest {
        coEvery { loginUseCase(TEST_EMAIL, TEST_PASSWORD) }
            .returns(AuthResult.Error("Credenciales incorrectas"))

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.login(TEST_EMAIL, TEST_PASSWORD)

            assertEquals(AuthResult.Error("Credenciales incorrectas"), awaitItem())
            ensureAllEventsConsumed()
        }
    }
}