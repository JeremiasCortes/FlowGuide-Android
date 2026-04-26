package com.jeremiascortes.flowguide.features.auth.presentation

/**
 * ============================================================================
 * CAPA: Presentation - Screen
 * ============================================================================
 *
 * Pantalla de registro de nuevos usuarios.
 *
 * CAMPOS DEL FORMULARIO:
 * - name: Nombre del usuario
 * - email: Email (será su identificador)
 * - birthday: Fecha de nacimiento (YYYY-MM-DD)
 * - password: Contraseña
 * - confirmPassword: Confirmación de contraseña
 *
 * VALIDACIONES:
 * - Todos los campos deben estar llenos
 * - Las contraseñas deben coincidir
 * - El botón se deshabilita si no se cumplen las validaciones
 *
 * VER TAMBIÉN:
 * - LoginScreen: Pantalla de inicio de sesión
 * - AuthViewModel: Lógica de autenticación
 * ============================================================================
 */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // Observamos el resultado de operaciones del ViewModel
    val authResult by viewModel.authResult.collectAsState()

    // Estados locales para los campos del formulario
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Efecto: Observar eventos de navegación del ViewModel (mismo patrón que LoginScreen)
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.ToHome -> onNavigateToHome()
                is NavigationEvent.ToLogin -> onNavigateToLogin()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo: Nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: Fecha de nacimiento
        OutlinedTextField(
            value = birthday,
            onValueChange = { birthday = it },
            label = { Text("Fecha de Nacimiento (YYYY-MM-DD)") },
            singleLine = true,
            placeholder = { Text("1990-01-01") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: Confirmar contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de registro (muestra loading si está en proceso)
        if (authResult is AuthResult.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    viewModel.register(name, email, birthday, password, confirmPassword)
                },
                enabled = name.isNotBlank() &&
                        email.isNotBlank() &&
                        birthday.isNotBlank() &&
                        password.isNotBlank() &&
                        confirmPassword.isNotBlank() &&
                        password == confirmPassword,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear Cuenta")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Google
        Button(
            onClick = { viewModel.signInWithGoogle() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse con Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace a login
        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: No mostrar errores en brutos, se tiene que mostrar con diálogos
        // Mostrar errores si los hay
        if (authResult is AuthResult.Error) {
            Text(
                text = (authResult as AuthResult.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        // Mostrar advertencia si las contraseñas no coinciden
        if (password.isNotBlank() && confirmPassword.isNotBlank() && password != confirmPassword) {
            Text(
                text = "Las contraseñas no coinciden",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
