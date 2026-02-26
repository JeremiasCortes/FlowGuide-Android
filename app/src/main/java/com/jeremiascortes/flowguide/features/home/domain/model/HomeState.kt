package com.jeremiascortes.flowguide.features.home.domain.model

import com.jeremiascortes.flowguide.features.home.data.model.FolderDto
import com.jeremiascortes.flowguide.features.home.data.model.ProcedureDto
import com.jeremiascortes.flowguide.features.home.data.model.SpaceDto

/**
 * Estado completo de la pantalla Home.
 * Agrupa todos los datos necesarios para renderizar la UI.
 */
data class HomeState(
    // Datos de cada nivel de la jerarquía
    val spaces: List<SpaceDto> = emptyList(),
    val folders: List<FolderDto> = emptyList(),
    val orphanProceduresBySpace: Map<String, List<ProcedureDto>> = emptyMap(),
    val proceduresByFolder: Map<String, List<ProcedureDto>> = emptyMap(),

    // IDs de los elementos seleccionados (para navegación)
    val selectedSpaceId: String? = null,
    val selectedFolderId: String? = null,
    val selectedProcedureId: String? = null,

    // Estados de UI
    val isLoading: Boolean = false,
    val error: String? = null,
    val expandedFolderIds: Set<String> = emptySet()  // IDs de carpetas abiertas
)