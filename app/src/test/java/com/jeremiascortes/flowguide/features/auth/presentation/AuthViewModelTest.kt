package com.jeremiascortes.flowguide.features.auth.presentation

import app.cash.turbine.test
import com.jeremiascortes.flowguide.MainDispatcherRule
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthState
import com.jeremiascortes.flowguide.features.auth.domain.usecase.GetCurrentSessionUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.LoginUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.LogoutUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.ObserveSessionStatusUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.RegisterUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.SignInWithGoogleUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
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
    private val getCurrentSessionUseCase: GetCurrentSessionUseCase = mockk()
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase = mockk()
    private val observeSessionStatusUseCase: ObserveSessionStatusUseCase = mockk()

    // Flow para simular cambios de sesión en los tests
    private val sessionStatusFlow = MutableStateFlow<AuthState>(AuthState.NotAuthenticated)

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
        coEvery { getCurrentSessionUseCase() }.returns(AuthState.NotAuthenticated)
        every { observeSessionStatusUseCase() }.returns(sessionStatusFlow)
        viewModel = AuthViewModel(
            loginUseCase = loginUseCase,
            registerUseCase = registerUseCase,
            logoutUseCase = logoutUseCase,
            getCurrentSessionUseCase = getCurrentSessionUseCase,
            signInWithGoogleUseCase = signInWithGoogleUseCase,
            observeSessionStatusUseCase = observeSessionStatusUseCase
        )
    }

    @Test
    fun `Primera vez que se registra un usuario, lo hace bien, sale exitoso el registro y navega a Home`() =
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

            viewModel.authResult.test {
                assertEquals(null, awaitItem())

                viewModel.register(
                    TEST_NAME,
                    TEST_EMAIL,
                    TETS_BIRTHDATE,
                    TEST_PASSWORD,
                    TEST_CONFIRM_PASSWORD
                )

//                advanceUntilIdle()

                assertEquals(AuthResult.Loading, awaitItem())

                advanceUntilIdle()

                assertEquals(AuthResult.Success(Unit), awaitItem())

                ensureAllEventsConsumed()
            }

            viewModel.navigationEvent.test {
                assertEquals(NavigationEvent.ToHome, awaitItem())
                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `El usuario se autentica correctamente y navega a la Home`() = runTest {
        coEvery {
            loginUseCase(
                TEST_EMAIL,
                TEST_PASSWORD
            )
        }.returns(AuthResult.Success(Unit))

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.login(TEST_EMAIL, TEST_PASSWORD)

            advanceUntilIdle()

            assertEquals(AuthResult.Loading, awaitItem())

            advanceUntilIdle()

            assertEquals(AuthResult.Success(Unit), awaitItem())

            ensureAllEventsConsumed()
        }

        viewModel.navigationEvent.test {
            assertEquals(NavigationEvent.ToHome, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `El usuario cierra sesion correctamente y navega a Login`() = runTest {
        coEvery { logoutUseCase() }.returns(AuthResult.Success(Unit))

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.logout()

            advanceUntilIdle()

            assertEquals(AuthResult.Loading, awaitItem())

            advanceUntilIdle()

            assertEquals(AuthResult.Success(Unit), awaitItem())

            ensureAllEventsConsumed()
        }

        viewModel.navigationEvent.test {
            assertEquals(NavigationEvent.ToLogin, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `El usuario se registra correctamente con Google y navega a Home`() = runTest {
        coEvery { signInWithGoogleUseCase() }.returns(AuthResult.Success(Unit))

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.signInWithGoogle()

            advanceUntilIdle()

            assertEquals(AuthResult.Loading, awaitItem())

            // Simular que el deep link callback llega y la sesión se establece
            sessionStatusFlow.value = AuthState.Authenticated("user-google-123")
            advanceUntilIdle()

            assertEquals(AuthResult.Success(Unit), awaitItem())

            ensureAllEventsConsumed()
        }

        viewModel.navigationEvent.test {
            assertEquals(NavigationEvent.ToHome, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `El usuario se autentica correctamente con Google y navega a la Home`() = runTest {
        coEvery { signInWithGoogleUseCase() }.returns(AuthResult.Success(Unit))

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.signInWithGoogle()

            advanceUntilIdle()

            assertEquals(AuthResult.Loading, awaitItem())

            // Simular que el deep link callback llega y la sesión se establece
            sessionStatusFlow.value = AuthState.Authenticated("user-google-456")
            advanceUntilIdle()

            assertEquals(AuthResult.Success(Unit), awaitItem())

            ensureAllEventsConsumed()
        }

        viewModel.navigationEvent.test {
            assertEquals(NavigationEvent.ToHome, awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `El usuario entra en la app y ya tenia session previa, se autentica correctamente y lo redirige a Home`() =
        runTest {
            coEvery { getCurrentSessionUseCase() }.returns(AuthState.Authenticated("user-123"))

            viewModel = AuthViewModel(
                loginUseCase = loginUseCase,
                registerUseCase = registerUseCase,
                logoutUseCase = logoutUseCase,
                getCurrentSessionUseCase = getCurrentSessionUseCase,
                signInWithGoogleUseCase = signInWithGoogleUseCase,
                observeSessionStatusUseCase = observeSessionStatusUseCase
            )

            viewModel.authState.test {
                assertEquals(AuthState.NotAuthenticated, awaitItem())

                advanceUntilIdle()

                assertEquals(AuthState.Authenticated("user-123"), awaitItem())

                ensureAllEventsConsumed()
            }

            viewModel.navigationEvent.test {
                assertEquals(NavigationEvent.ToHome, awaitItem())
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

            advanceUntilIdle()

            assertEquals(AuthResult.Loading, awaitItem())

            advanceUntilIdle()

            assertEquals(AuthResult.Error("Error al registrar"), awaitItem())

            ensureAllEventsConsumed()
        }

        viewModel.navigationEvent.test {
            expectNoEvents()
        }
    }

    @Test
    fun `login fallido emite Loading, luego Error, y no navega`() = runTest {
        coEvery { loginUseCase(TEST_EMAIL, TEST_PASSWORD) }.coAnswers {
            AuthResult.Error("Credenciales incorrectas")
        }

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.login(TEST_EMAIL, TEST_PASSWORD)

            advanceUntilIdle()
            assertEquals(AuthResult.Loading, awaitItem())

            advanceUntilIdle()
            assertEquals(AuthResult.Error("Credenciales incorrectas"), awaitItem())

            ensureAllEventsConsumed()
        }

        viewModel.navigationEvent.test {
            expectNoEvents()
        }
    }

    @Test
    fun `Login con google fallido, emite Loading, luego Error, y no navega`() = runTest {
        coEvery { signInWithGoogleUseCase() }.coAnswers {
            AuthResult.Error("Hubo un error")
        }

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.signInWithGoogle()

            advanceUntilIdle()
            assertEquals(AuthResult.Loading, awaitItem())

            advanceUntilIdle()
            assertEquals(AuthResult.Error("Hubo un error"), awaitItem())

            ensureAllEventsConsumed()
        }

        viewModel.navigationEvent.test {
            expectNoEvents()
        }
    }

    @Test
    fun `logout fallido, emite Loading, luego Error, y no navega`() = runTest {
        coEvery { logoutUseCase() }.coAnswers {
            AuthResult.Error("Hubo un error")
        }

        viewModel.authResult.test {
            assertEquals(null, awaitItem())

            viewModel.logout()

            advanceUntilIdle()
            assertEquals(AuthResult.Loading, awaitItem())

            advanceUntilIdle()
            assertEquals(AuthResult.Error("Hubo un error"), awaitItem())

            ensureAllEventsConsumed()
        }

        viewModel.navigationEvent.test {
            expectNoEvents()
        }
    }
}