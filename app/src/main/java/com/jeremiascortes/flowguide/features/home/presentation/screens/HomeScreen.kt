package com.jeremiascortes.flowguide.features.home.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.jeremiascortes.flowguide.features.home.presentation.components.common.EmptyFolderState
import com.jeremiascortes.flowguide.features.home.presentation.components.folder.FolderList
import com.jeremiascortes.flowguide.features.home.presentation.components.space.SpaceTabs
import com.jeremiascortes.flowguide.features.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigateToProcedure: (String) -> Unit,
) {
    val state by homeViewModel.state.collectAsState()

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // SECCIÓN 1: Spaces (tabs horizontales)
            SpaceTabs(
                spaces = state.spaces,
                selectedSpaceId = state.selectedSpaceId,
                onSpaceSelected = { id ->
                    homeViewModel.selectSpace(id)
                    homeViewModel.loadProcedures(id)
                }
            )

            if (state.error != null) {
                Text("Ocurrió un error al cargar los espacios. \n ${state.error}")
            }

            if (state.folders.isNotEmpty()) {
                FolderList(
                    folders = state.folders,
                    expandedFolderIds = state.expandedFolderIds,
                    proceduresByFolder = state.proceduresByFolder,
                    onToggleFolder = { folderId ->
                        homeViewModel.toggleFolder(folderId)
                    },
                    onNavigateToProcedure = onNavigateToProcedure
                )
            } else if (state.selectedSpaceId != null && !state.isLoading) {
                EmptyFolderState()
            }
        }
    }
}