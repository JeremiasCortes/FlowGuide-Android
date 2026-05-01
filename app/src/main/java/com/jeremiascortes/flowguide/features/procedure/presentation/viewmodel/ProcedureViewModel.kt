package com.jeremiascortes.flowguide.features.procedure.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeremiascortes.flowguide.features.procedure.domain.model.CheckboxNode
import com.jeremiascortes.flowguide.features.procedure.domain.model.Procedure
import com.jeremiascortes.flowguide.features.procedure.domain.model.ProcedureState
import com.jeremiascortes.flowguide.features.procedure.domain.model.Step
import com.jeremiascortes.flowguide.features.procedure.domain.usecase.GetProcedureWithStepsUseCase
import com.jeremiascortes.flowguide.features.procedure.domain.usecase.UpdateStepCompletion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProcedureViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProcedureWithStepsUseCase: GetProcedureWithStepsUseCase,
    private val toggleStepCompletionUseCase: UpdateStepCompletion
) : ViewModel() {
    private val procedureId: String = savedStateHandle["procedureId"] ?: ""
    private val _state = MutableStateFlow(ProcedureState())
    val state: StateFlow<ProcedureState> = _state.asStateFlow()

    init {
        if (procedureId.isNotEmpty()) {
            loadProcedure(procedureId)
        }
    }

    /**
     * Ejecuta un bloque suspendido solo si actualmente hay un procedimiento cargado.
     *
     * Esta helper evita repetir el mismo null-check sobre `_state.value.procedure`
     * en varios métodos del ViewModel.
     *
     * Beneficios:
     * - Reduce código duplicado
     * - Hace más legible la intención del método
     * - Garantiza que dentro del bloque `procedure` nunca es null
     *
     * Si no hay procedimiento cargado, la función termina sin hacer nada.
     */
    private suspend fun withProcedure(block: suspend (procedure: Procedure) -> Unit) {
        val procedure = _state.value.procedure ?: return
        block(procedure)
    }

    fun loadProcedure(idProcedure: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val result = getProcedureWithStepsUseCase(idProcedure)
            result.onSuccess { procedure ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    procedure = procedure
                )
            }.onFailure { exception ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = exception.message
                )
            }

        }
    }

    // Método para establecer el estado loading
    fun setLoading(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }

    private fun Step.toCheckboxNode(index: Int): CheckboxNode {
        return CheckboxNode(
            id = index, // 1, 2, 3, 4...
            label = name,
            isChecked = isCompleted,
            idStep = id, // ESTE es el ID real del step de Supabase
            children = substeps.mapIndexed { idx, child -> child.toCheckboxNode(idx) }
        )
    }

    fun toggleStepCompletion(stepId: String, newValue: Boolean) {
        viewModelScope.launch {
            withProcedure { currentProcedure ->
                setLoading(true)

                val updatedSteps = currentProcedure.steps.map { step ->
                    if (step.id == stepId) step.copy(isCompleted = newValue) else step
                }

                _state.value = _state.value.copy(
                    procedure = currentProcedure.copy(steps = updatedSteps)
                )

                setLoading(false)

                toggleStepCompletionUseCase(stepId, newValue)
            }
        }
    }

    fun resetAllStepsCompletion() {
        viewModelScope.launch {
            withProcedure { currentProcedure ->
                setLoading(true)

                val updatedSteps = currentProcedure.steps.map { step ->
                    step.copy(isCompleted = false)
                }

                _state.value = _state.value.copy(
                    procedure = currentProcedure.copy(steps = updatedSteps)
                )

                setLoading(false)

                updatedSteps.forEach { step ->
                    toggleStepCompletionUseCase(step.id, false)
                }
            }
        }
    }
}