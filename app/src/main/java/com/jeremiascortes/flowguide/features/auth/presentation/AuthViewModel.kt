package com.jeremiascortes.flowguide.features.auth.presentation

/**
 * ============================================================================
 * CAPA: Presentation - ViewModel
 * ============================================================================
 *
 * ViewModel que gestiona el estado de autenticación de la aplicación.
 *
 * PATRÓN MVVM:
 * - View (LoginScreen, RegisterScreen) observa los StateFlows
 * - ViewModel expone estados y métodos para interactuar
 * - Los UseCases encapsulan la lógica de negocio
 *
 * STATE MANAGEMENT:
 * - authState: Estado de autenticación (logueado/no logueado)
 * - authResult: Resultado de la última operación (login, register, etc.)
 * - Se usa StateFlow para que la UI observe los cambios
 *
 * VIEWMODEL COMPARTIDO:
 * Este ViewModel se usa tanto en LoginScreen como en RegisterScreen y HomeScreen.
 * Al usar hiltViewModel() en diferentes pantallas, se crea la MISMA instancia
 * porque el scope es el NavController (scoped to navigation graph).
 *
 * CICLO DE VIDA:
 * - init: Verifica si hay sesión activa al crear el ViewModel
 * - Después de login/register exitoso: Verifica estado de autenticación
 * - Después de logout: Verifica estado (debería ser NotAuthenticated)
 * ============================================================================
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthState
import com.jeremiascortes.flowguide.features.auth.domain.usecase.GetCurrentSessionUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.LoginUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.LogoutUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.RegisterUseCase
import com.jeremiascortes.flowguide.features.auth.domain.usecase.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// Dentro del mismo archivo o en un archivo separado
sealed class NavigationEvent {
    data object ToHome : NavigationEvent()
    data object ToLogin : NavigationEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentSessionUseCase: GetCurrentSessionUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    // Estado de autenticación: Loading, Authenticated, NotAuthenticated
    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Resultado de la última operación: Success, Error, Loading
    private val _authResult = MutableStateFlow<AuthResult<Unit>?>(null)
    val authResult: StateFlow<AuthResult<Unit>?> = _authResult.asStateFlow()

    // Canal de eventos de navegación - garantiza entrega aunque el receptor llegue tarde
    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        // Al crear el ViewModel, verificar si ya hay sesión activa
        viewModelScope.launch {
            checkAuthStatus()
        }
    }

    /**
     * Verifica el estado actual de la sesión y lo retorna.
     *
     * IMPORTANTE: Es suspend (no lanza coroutine nueva) para que el llamador
     * pueda verificar el resultado ANTES de tomar decisiones (como navegar).
     *
     * El antiguo checkAuthStatus() usaba viewModelScope.launch, lo que creaba
     * una race condition: la navegación ocurría antes de verificar la sesión.
     */
    private suspend fun checkAuthStatus(): AuthState {
        val state = getCurrentSessionUseCase()
        _authState.value = state
        if (state is AuthState.Authenticated) {
            _navigationEvent.send(NavigationEvent.ToHome)
        }
        return state
    }

    /**
     * Inicia sesión con email y contraseña.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            val result = loginUseCase(email, password)
            _authResult.value = result
            if (result is AuthResult.Success) {
                _navigationEvent.send(NavigationEvent.ToHome)
            }
        }
    }

    /**
     * Autenticación con Google.
     * Funciona tanto para login como para registro (si no existe, lo crea).
     */
    fun signInWithGoogle() {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            _authResult.value = signInWithGoogleUseCase()
            if (_authResult.value is AuthResult.Success) {
                _navigationEvent.send(NavigationEvent.ToHome)
            }
        }
    }

    /**
     * Registra un nuevo usuario.
     */
    fun register(
        name: String,
        email: String,
        birthday: String,
        password: String,
        confirmPassword: String
    ) {
        // TODO: Validar campos, manejar errores, etc.

        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            _authResult.value = registerUseCase(name, email, birthday, password, confirmPassword)
            if (_authResult.value is AuthResult.Success) {
                _navigationEvent.send(NavigationEvent.ToHome)
            }
        }
    }

    /**
     * Cierra la sesión del usuario.
     */
    fun logout() {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            _authResult.value = logoutUseCase()
            // Actualiza el estado (siempre navega a login después)
            checkAuthStatus()
            _navigationEvent.send(NavigationEvent.ToLogin)
        }
    }
}
