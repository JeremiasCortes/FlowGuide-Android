package com.jeremiascortes.flowguide.features.home.presentation.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EmptyFolderState() {
    // Box es útil para superponer elementos o centrarlos en un espacio
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f), // Ocupa el 80% del alto restante para que se vea centrado respecto al área de contenido
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay carpetas disponibles en este espacio",
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}