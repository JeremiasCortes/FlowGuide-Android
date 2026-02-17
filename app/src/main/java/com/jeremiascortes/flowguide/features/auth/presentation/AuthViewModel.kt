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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentSessionUseCase: GetCurrentSessionUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    // Estado de autenticación: Loading, Authenticated, NotAuthenticated
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Resultado de la última operación: Success, Error, Loading
    private val _authResult = MutableStateFlow<AuthResult<Unit>?>(null)
    val authResult: StateFlow<AuthResult<Unit>?> = _authResult.asStateFlow()

    init {
        // Al crear el ViewModel, verificar si ya hay sesión activa
        checkAuthStatus()
    }

    /**
     * Verifica el estado actual de la sesión.
     * Se llama al iniciar y después de operaciones de autenticación.
     */
    private fun checkAuthStatus() {
        viewModelScope.launch {
            _authState.value = getCurrentSessionUseCase()
        }
    }

    /**
     * Inicia sesión con email y contraseña.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            _authResult.value = loginUseCase(email, password)
            if (_authResult.value is AuthResult.Success) {
                checkAuthStatus()
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
                checkAuthStatus()
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
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            _authResult.value = registerUseCase(name, email, birthday, password, confirmPassword)
            if (_authResult.value is AuthResult.Success) {
                checkAuthStatus()
            }
        }
    }

    /**
     * Cierra la sesión del usuario.
     */
    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            checkAuthStatus()
        }
    }

    /**
     * Limpia el resultado de la última operación.
     * Útil para ocultar mensajes de error después de mostrarlos.
     */
    fun clearResult() {
        _authResult.value = null
    }
}
