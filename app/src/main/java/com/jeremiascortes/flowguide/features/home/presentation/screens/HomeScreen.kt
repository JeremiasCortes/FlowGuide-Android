package com.jeremiascortes.flowguide.features.home.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jeremiascortes.flowguide.features.home.domain.model.HomeDialogType
import com.jeremiascortes.flowguide.features.home.presentation.components.common.EmptyFolderState
import com.jeremiascortes.flowguide.features.home.presentation.components.folder.FolderList
import com.jeremiascortes.flowguide.features.home.presentation.components.space.SpaceTabs
import com.jeremiascortes.flowguide.features.home.presentation.viewmodel.HomeViewModel
import com.jeremiascortes.flowguide.presentation.components.LoadingIndicator

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigateToProcedure: (String) -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    val state by homeViewModel.state.collectAsState()
    var textInput by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = bottomBar
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                // SECCIÓN 1: Spaces (tabs horizontales)
                SpaceTabs(
                    spaces = state.spaces,
                    selectedSpaceId = state.selectedSpaceId,
                    onSpaceSelected = { id ->
                        if (id != "CrearSpace") {
                            homeViewModel.selectSpace(id)
                        } else {
                            homeViewModel.createSpaceDialog()
                        }
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
                        onNavigateToProcedure = onNavigateToProcedure,
                        newProcedure = {
                            homeViewModel.createProcedureDialog()
                        }
                    )
                } else if (state.selectedSpaceId != null && !state.isLoading) {
                    EmptyFolderState()
                }
            }

            LoadingIndicator(isLoading = state.isLoading)

            if (state.activeDialog) {
                when (state.dialogType) {
                    HomeDialogType.CREATE_SPACE -> {
                        AlertDialogExample(
                            onDismissRequest = {
                                homeViewModel.closeDialog()
                                textInput = ""
                            },
                            onConfirmation = {
                                homeViewModel.createSpace(textInput)
                                textInput = ""
                            },
                            dialogTitle = "Crear un espacio",
                            dialogText = "Introduce el nombre del espacio que deseas crear",
                            content = {
                                OutlinedTextField(
                                    value = textInput,
                                    onValueChange = { textInput = it },
                                    label = { Text("Nombre del espacio") },
                                    singleLine = true
                                )
                            },
                            icon = Icons.Default.Info
                        )
                    }

                    HomeDialogType.CREATE_FOLDER -> {
                        AlertDialogExample(
                            onDismissRequest = {
                                homeViewModel.closeDialog()
                                textInput = ""
                            },
                            onConfirmation = {
                                state.selectedSpaceId?.let { spaceId ->
                                    homeViewModel.createFolder(textInput, spaceId)
                                }
                                textInput = ""
                            },
                            dialogTitle = "Crear una carpeta",
                            dialogText = "Introduce el nombre de la carpeta que deseas crear",
                            content = {
                                OutlinedTextField(
                                    value = textInput,
                                    onValueChange = { textInput = it },
                                    label = { Text("Nombre de la carpeta") },
                                    singleLine = true
                                )
                            },
                            icon = Icons.Default.Info
                        )
                    }

                    HomeDialogType.CREATE_PROCEDURE -> {
                        AlertDialogExample(
                            onDismissRequest = {
                                homeViewModel.closeDialog()
                                textInput = ""
                            },
                            onConfirmation = {
                                homeViewModel.createProcedure()
                                textInput = ""
                            },
                            dialogTitle = "Crear un procedimiento",
                            dialogText = "Introduce el nombre del procedimiento que deseas crear",
                            content = {
                                OutlinedTextField(
                                    value = textInput,
                                    onValueChange = { textInput = it },
                                    label = { Text("Nombre del procedimiento") },
                                    singleLine = true
                                )
                            },
                            icon = Icons.Default.Info
                        )
                    }

                    else -> {} // ningún diálogo
                }
            }
        }
    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    content: @Composable () -> Unit,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column() {
                Text(text = dialogText)
                Spacer(Modifier.width(2.dp))
                content()
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}