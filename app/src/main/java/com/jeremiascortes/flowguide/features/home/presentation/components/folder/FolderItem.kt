package com.jeremiascortes.flowguide.features.home.presentation.components.folder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeremiascortes.flowguide.features.home.data.model.FolderDto
import com.jeremiascortes.flowguide.features.home.data.model.ProcedureDto

@Composable
fun FolderItem(
    folder: FolderDto,
    isExpanded: Boolean,           // ← Estado que viene de afuera (hoisting)
    procedures: List<ProcedureDto>, // ← Procedures de esta carpeta
    onToggle: () -> Unit           // ← Callback para cambiar estado
) {
    Column {
        // Header de la carpeta (clickable)
        ListItem(
            headlineContent = { Text(folder.title) },
            supportingContent = { Text("Actualizado: ${folder.updatedAt}") },
            trailingContent = {
                // Icono que indica si está abierto o cerrado
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess
                    else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            },
            modifier = Modifier.clickable { onToggle() }
        )

        // Contenido expandible con animación
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                if (procedures.isEmpty()) {
                    // Estado vacío
                    Text(
                        text = "No hay procedimientos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    procedures.forEach { procedure ->
                        ProcedureItem(procedure = procedure)
                    }
                }
            }
        }
    }
}