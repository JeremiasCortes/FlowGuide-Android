package com.jeremiascortes.flowguide.features.procedure.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeremiascortes.flowguide.features.procedure.domain.model.CheckboxNode
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
    private val savedStateHandle: SavedStateHandle,
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
            // Primero actualizamos el estado local para que la UI responda rápido
            val currentProcedure = _state.value.procedure ?: return@launch
            val updatedSteps = currentProcedure.steps.map { step ->
                if (step.id == stepId) {
                    step.copy(isCompleted = newValue)
                } else {
                    step
                }
            }

            _state.value = _state.value.copy(
                isLoading = true,
                procedure = currentProcedure.copy(steps = updatedSteps)
            )

            toggleStepCompletionUseCase(stepId, newValue)
        }
    }
}