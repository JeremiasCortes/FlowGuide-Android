package com.jeremiascortes.flowguide.features.home.presentation.components.space

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeremiascortes.flowguide.features.home.data.model.SpaceDto

@Composable
fun SpaceTabs(
    spaces: List<SpaceDto>,
    selectedSpaceId: String?,
    onSpaceSelected: (String) -> Unit
) {
    // Scroll horizontal para los espacios
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(spaces) { space ->
            SpaceChip(
                space = space,
                isSelected = space.idSpace == selectedSpaceId,
                onClick = { onSpaceSelected(space.idSpace) }
            )
        }
    }
}