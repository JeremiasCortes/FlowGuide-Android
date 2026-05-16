package com.jeremiascortes.flowguide.features.home.presentation.viewmodel

/**
 * ============================================================================
 * CAPA: Presentation - ViewModel
 * ============================================================================
 *
 * ViewModel que gestiona el estado de la pantalla Home.
 *
 * PATRÓN MVVM:
 * - View (HomeScreen) observa los StateFlows
 * - ViewModel expone estados y métodos para interactuar
 * - Los UseCases encapsulan la lógica de negocio
 *
 * NAVEGACIÓN JERÁRQUICA:
 * Spaces → Folders → Procedures → Steps
 * Cada nivel carga sus datos cuando se selecciona el padre.
 *
 * STATE MANAGEMENT:
 * - HomeState: Data class que agrupa el estado completo de la pantalla
 * - _state: MutableStateFlow interno (solo el ViewModel puede modificar)
 * - state: StateFlow público expuesto a la UI (read-only)
 * ============================================================================
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeremiascortes.flowguide.features.home.data.model.SpaceDto
import com.jeremiascortes.flowguide.features.home.domain.model.HomeDialogType
import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult
import com.jeremiascortes.flowguide.features.home.domain.model.HomeState
import com.jeremiascortes.flowguide.features.home.domain.usecase.CreateFolderUseCase
import com.jeremiascortes.flowguide.features.home.domain.usecase.CreateSpaceUseCase
import com.jeremiascortes.flowguide.features.home.domain.usecase.GetAllFoldersByIdSpaceUseCase
import com.jeremiascortes.flowguide.features.home.domain.usecase.GetAllProceduresByIdFolderUseCase
import com.jeremiascortes.flowguide.features.home.domain.usecase.GetAllProceduresByIdSpaceUseCase
import com.jeremiascortes.flowguide.features.home.domain.usecase.GetAllSpacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllSpacesUseCase: GetAllSpacesUseCase,
    private val getAllFoldersByIdSpaceUseCase: GetAllFoldersByIdSpaceUseCase,
    private val getAllProceduresByIdSpaceUseCase: GetAllProceduresByIdSpaceUseCase,
    private val getAllProceduresByIdFolderUseCase: GetAllProceduresByIdFolderUseCase,
    private val createSpaceUseCase: CreateSpaceUseCase,
    private val createFolderUseCase: CreateFolderUseCase,
) : ViewModel() {

    // Backing property: MutableStateFlow privado (solo el ViewModel puede escribir)
    private val _state = MutableStateFlow(HomeState())


    // StateFlow público de solo lectura para la UI
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        // Cargar los espacios al iniciar el ViewModel
        loadSpaces()
    }

    fun loadSpaces() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            when (val result = getAllSpacesUseCase()) {
                // El loading ya se maneja al inicio de la función, aquí no se requiere acción adicional
                is HomeResult.Loading -> Unit

                is HomeResult.Success -> {

                    _state.value = _state.value.copy(
                        isLoading = false,
                        spaces = result.data + SpaceDto(
                            idSpace = "CrearSpace",
                            title = "+ Crear un espacio...",
                            userId = "CrearSpace",
                            createdAt = "",
                            updatedAt = ""
                        )
                    )

                    // Seleccionamos automáticamente el primer espacio disponible
                    result.data.firstOrNull()?.let { firstSpace ->
                        selectSpace(firstSpace.idSpace)
                    }
                }

                is HomeResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun toggleFolder(folderId: String) {
        // Se obtiene la lista actual de carpetas expandidas
        val currentExpanded = _state.value.expandedFolderIds

        // ¿La carpeta que se está expandiendo o contrae ya estaba abierta?
        val isCurrentlyExpanded = folderId in currentExpanded


        // Se actualiza el estado de la carpeta seleccionada
        // Si no está expandida, se expande; si está expandida, se contrae
        val newExpanded = if (isCurrentlyExpanded) {
            currentExpanded - folderId  // Cerrar
        } else {
            currentExpanded + folderId  // Abrir
        }

        // Actualizar el estado interno
        _state.value = _state.value.copy(expandedFolderIds = newExpanded)

        if (!isCurrentlyExpanded) {
            viewModelScope.launch {
                when (val result = getAllProceduresByIdFolderUseCase(folderId)) {
                    // El loading no aplica en esta operación asíncrona
                    is HomeResult.Loading -> Unit
                    is HomeResult.Success -> {
                        _state.value = _state.value.copy(
                            proceduresByFolder = _state.value.proceduresByFolder + (folderId to result.data)
                        )
                    }

                    is HomeResult.Error -> Unit
                }
            }
        }
    }

    /**
     * Selecciona un espacio y carga sus carpetas.
     */
    fun selectSpace(spaceId: String) {
        viewModelScope.launch {
            // Actualizar el ID seleccionado
            _state.value = _state.value.copy(
                isLoading = true,
                selectedSpaceId = spaceId,
                // Limpiar selecciones hijas (si cambias de espacio, ya no tienes carpeta/procedimiento)
                selectedFolderId = null,
                selectedProcedureId = null,
                folders = emptyList(),
                proceduresByFolder = emptyMap(),
                expandedFolderIds = emptySet()
            )

            // Cargar carpetas del espacio seleccionado
            when (val result = getAllFoldersByIdSpaceUseCase(spaceId)) {
                // El loading ya se activó al inicio de selectSpace()
                is HomeResult.Loading -> Unit

                is HomeResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        folders = result.data,
                        error = null
                    )
                }

                is HomeResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun showDialog() {
        _state.value = state.value.copy(
            activeDialog = true
        )
    }

    fun closeDialog() {
        _state.value = state.value.copy(
            activeDialog = false,
            dialogType = null
        )
    }

    fun createSpaceDialog() {
        showDialog()
        _state.value = state.value.copy(
            dialogType = HomeDialogType.CREATE_SPACE
        )
    }

    fun createSpace(nameSpace: String) {
        closeDialog()
        viewModelScope.launch {
            when (val result = createSpaceUseCase(nameSpace)) {
                is HomeResult.Success -> {
                    loadSpaces()
                }

                is HomeResult.Error -> {
                    TODO()
                }

                HomeResult.Loading -> {
                    TODO()
                }
            }
        }
    }

    fun createFolderDialog() {
        showDialog()
        _state.value = state.value.copy(
            dialogType = HomeDialogType.CREATE_FOLDER
        )
    }

    fun createFolder(nameFolder: String, spaceId: String) {
        closeDialog()
        viewModelScope.launch {
            when (val result = createFolderUseCase(
                nameFolder = nameFolder,
                spaceId = spaceId
            )) {
                is HomeResult.Success -> {
                    selectSpace(spaceId)
                }

                is HomeResult.Error -> {
                    TODO()
                }

                HomeResult.Loading -> {
                    TODO()
                }
            }
        }
    }

    fun createProcedureDialog() {
        showDialog()
        _state.value = state.value.copy(
            dialogType = HomeDialogType.CREATE_PROCEDURE
        )
    }

    fun createProcedure() {
        closeDialog()
    }
}
