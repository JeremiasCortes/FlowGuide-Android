package com.jeremiascortes.flowguide.features.home.presentation

/**
 * ============================================================================
 * CAPA: Presentation
 * ============================================================================
 *
 * Esta pantalla representa la pantalla principal de la aplicación después de
 * que el usuario se ha autenticado correctamente.
 *
 * ARQUITECTURA MVVM:
 * - View (este archivo): Muestra la UI y observa estados del ViewModel
 * - ViewModel: AuthViewModel (compartido con la feature de auth)
 * - No tiene caso de uso propio porque solo muestra información básica
 *
 * RESPONSABILIDADES:
 * - Verificar que el usuario está autenticado antes de mostrar contenido
 * - Manejar el gesto back para minimizar la app (no volver al navegador)
 * - Permitir cerrar sesión
 *
 * NOTA: En el futuro, si Home necesita lógica de negocio compleja,
 * debería tener su propio HomeViewModel en lugar de usar AuthViewModel.
 * ============================================================================
 */

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavKey
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthState
import com.jeremiascortes.flowguide.features.auth.presentation.AuthViewModel
import kotlinx.serialization.Serializable

/**
 * Ruta de navegación type-safe para Home.
 * Se usa con Navigation Compose y kotlinx.serialization.
 */
@Serializable
data object Home : NavKey

/**
 * Pantalla principal de la aplicación.
 *
 * @param viewModel AuthViewModel para gestionar el estado de autenticación
 * @param onNavigateToSplash Callback para navegar al Splash (usado al cerrar sesión)
 */
@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    onNavigateToSplash: () -> Unit,
) {
    // Observamos el estado de autenticación desde el ViewModel
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    // BackHandler: Intercepta el botón/gesto back del sistema
    // IMPORTANTE: Después del OAuth, el navegador queda en la pila de actividades.
    // Sin esto, el usuario volvería al navegador al presionar back.
    // Con esto, minimizamos la app (comportamiento esperado en pantalla principal)
    BackHandler(enabled = true) {
        (context as? Activity)?.moveTaskToBack(true)
    }

    // Efecto para verificar autenticación: si no está autenticado, navegar al Splash
    LaunchedEffect(authState) {
        if (authState !is AuthState.Authenticated) {
            onNavigateToSplash()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(text = "Bienvenido a la pantalla principal")

            // TODO: Eliminar este botón en producción, es solo para pruebas
            Button(onClick = { onNavigateToSplash() }) {
                Text("Volver al Splash")
            }

            Button(onClick = { viewModel.logout() }) {
                Text("Cerrar sesión")
            }
        }
    }
}
