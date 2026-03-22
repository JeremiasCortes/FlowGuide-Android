package com.jeremiascortes.flowguide.presentation.components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Modelo que representa un item de acción en el BottomBar.
 */
data class BottomBarItem(
    val icon: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true
)