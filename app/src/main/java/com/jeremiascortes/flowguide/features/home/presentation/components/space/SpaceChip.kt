package com.jeremiascortes.flowguide.features.home.presentation.components.space

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeremiascortes.flowguide.features.home.data.model.SpaceDto

@Composable
fun SpaceChip(
    space: SpaceDto,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // FilterChip cambia de aspecto cuando está seleccionado
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(space.title) },
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}