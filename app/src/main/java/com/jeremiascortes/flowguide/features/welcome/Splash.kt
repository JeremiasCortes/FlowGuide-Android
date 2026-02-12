package com.jeremiascortes.flowguide.features.welcome

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jeremiascortes.flowguide.R
import kotlinx.coroutines.delay

@Composable
fun Splash(onNavigateToHome: () -> Unit) {
    // Animación de entrada
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

    LaunchedEffect(Unit) {
        // Esperar 2 segundos y navegar
        delay(2500)
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
            // Puedes poner cualquier imagen, icono, video, etc.
//            Image(
//                painter = painterResource(R.drawable.logo),
//                contentDescription = "Logo",
//                modifier = Modifier.size(120.dp)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "FlowGuide",
//                style = MaterialTheme.typography.headlineMedium,
//                color = Color.White
//            )

            LottieAnimationComponent()

            // Puedes agregar un loader
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@Composable
fun LottieAnimationComponent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ripple_loading_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 3
    )

    LottieAnimation(
        modifier = Modifier.fillMaxSize(.5f),
        composition = composition,
        progress = { progress }
    )
}