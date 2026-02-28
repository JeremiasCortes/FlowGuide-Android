package com.jeremiascortes.flowguide.features.home.presentation.screen

/**
 * ============================================================================
 * CAPA: Presentation
 * ============================================================================
 *
 * Esta pantalla representa la pantalla principal de la aplicación después de
 * que el usuario se ha autenticado correctamente.
 *
 * ARQUITECTURA MVVM:
 * - View (este archivo): Muestra la UI y observa estados del ViewModel
 * - ViewModel: AuthViewModel (compartido con la feature de auth)
 * - No tiene caso de uso propio porque solo muestra información básica
 *
 * RESPONSABILIDADES:
 * - Verificar que el usuario está autenticado antes de mostrar contenido
 * - Manejar el gesto back para minimizar la app (no volver al navegador)
 * - Permitir cerrar sesión
 *
 * NOTA: En el futuro, si Home necesita lógica de negocio compleja,
 * debería tener su propio HomeViewModel en lugar de usar AuthViewModel.
 * ============================================================================
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.jeremiascortes.flowguide.features.home.presentation.components.common.EmptyFolderState
import com.jeremiascortes.flowguide.features.home.presentation.components.folder.FolderList
import com.jeremiascortes.flowguide.features.home.presentation.components.folder.ProcedureItem
import com.jeremiascortes.flowguide.features.home.presentation.components.space.SpaceTabs
import com.jeremiascortes.flowguide.features.home.presentation.viewmodel.HomeViewModel
import kotlinx.serialization.Serializable

/**
 * Ruta de navegación type-safe para Home.
 * Se usa con Navigation Compose y kotlinx.serialization.
 */
@Serializable
data object Home : NavKey

/**
 * Pantalla principal de la aplicación.
 *
 * @param homeViewModel HomeViewModel para gestionar el estado
 * @param onNavigateToSplash Callback para navegar al Splash (usado al cerrar sesión)
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavigateToSplash: () -> Unit,
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

            if (state.orphanProceduresBySpace.isNotEmpty()) {
                Column(modifier = Modifier.padding(start = 32.dp)) {
                    val procedures = state.orphanProceduresBySpace[state.selectedSpaceId] ?: emptyList()

                    procedures.forEach { procedure ->
                        ProcedureItem(procedure = procedure)
                    }
                }
            }

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
                    }
                )
            } else if (state.selectedSpaceId != null && !state.isLoading && state.orphanProceduresBySpace.isEmpty()) {
                EmptyFolderState()
            }
        }
    }
}