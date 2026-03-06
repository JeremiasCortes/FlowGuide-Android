package com.jeremiascortes.flowguide.features.home.presentation.components.folder

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeremiascortes.flowguide.features.home.data.model.FolderDto
import com.jeremiascortes.flowguide.features.home.data.model.ProcedureDto

@Composable
fun FolderList(
    folders: List<FolderDto>,
    expandedFolderIds: Set<String>,
    proceduresByFolder: Map<String, List<ProcedureDto>>,
    onToggleFolder: (String) -> Unit,
    onNavigateToProcedure: (String) -> Unit
) {
    // LazyColumn es el equivalente a RecyclerView, ideal para listas largas
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(folders) { folder ->
            // ¿Está esta carpeta expandida?
            val isExpanded = folder.idFolder in expandedFolderIds

            // ¿Cuáles son los procedures de ESTA carpeta?
            val procedures = proceduresByFolder[folder.idFolder] ?: emptyList()

            FolderItem(
                folder = folder,
                isExpanded = isExpanded,
                procedures = procedures,
                onToggle = { onToggleFolder(folder.idFolder) },
                onNavigateToProcedure = onNavigateToProcedure
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}