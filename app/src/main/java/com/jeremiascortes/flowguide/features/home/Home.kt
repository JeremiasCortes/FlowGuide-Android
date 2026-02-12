package com.jeremiascortes.flowguide.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import io.github.jan.supabase.realtime.Column
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Composable
fun Home(onNavigateToSplash: () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column {
            Text(
                text = "Bienvenido a la pantalla principal",
                modifier = Modifier.padding(innerPadding)
            )

            Button(onClick = { onNavigateToSplash() }) {
                Text("Volver al Splash")
            }
        }

    }
}