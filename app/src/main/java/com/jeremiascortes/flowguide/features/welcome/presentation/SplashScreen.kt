package com.jeremiascortes.flowguide.features.welcome.presentation

/**
 * ============================================================================
 * CAPA: Presentation
 * ============================================================================
 *
 * Pantalla de bienvenida que se muestra al iniciar la aplicación.
 *
 * ARQUITECTURA MVVM:
 * - View (este archivo): Muestra animación de carga
 * - No tiene ViewModel porque no maneja estado complejo
 * - No tiene casos de uso porque no realiza operaciones de negocio
 *
 * RESPONSABILIDADES:
 * - Mostrar animación de marca/bienvenida
 * - Esperar un tiempo determinado antes de navegar a la siguiente pantalla
 *
 * MEJORAS FUTURAS:
 * - Verificar sesión activa durante el splash y navegar directamente a Home
 * - Cargar recursos necesarios mientras se muestra la animación
 * ============================================================================
 */

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import androidx.compose.runtime.*
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jeremiascortes.flowguide.R
import kotlinx.coroutines.delay

/**
 * Pantalla de Splash que muestra una animación de carga.
 *
 * @param onNavigateToHome Callback para navegar a la siguiente pantalla
 *        después de que termine la animación/tiempo de espera
 */
@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    // Animaciones de entrada suaves
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800),
        label = "alpha"
    )

    // Efecto para navegar después del tiempo de espera
    LaunchedEffect(Unit) {
        delay(3000) // 3 segundos de splash
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
        ) {
            // Componente de animación Lottie
            LottieAnimationComponent()
        }
    }
}

/**
 * Componente que reproduce la animación Lottie del splash.
 * Usa el archivo raw/ripple_loading_animation.json
 */
@Composable
private fun LottieAnimationComponent() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.ripple_loading_animation)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 3 // Repite 3 veces la animación
    )

    LottieAnimation(
        modifier = Modifier.fillMaxSize(0.5f),
        composition = composition,
        progress = { progress }
    )
}
