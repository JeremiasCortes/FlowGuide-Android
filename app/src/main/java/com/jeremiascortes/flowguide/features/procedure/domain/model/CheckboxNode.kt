package com.jeremiascortes.flowguide.features.procedure.domain.model

import androidx.compose.ui.state.ToggleableState

data class CheckboxNode(
    val id: Int,
    val label: String,
    val isChecked: Boolean = false,
    val idStep: String,
    val children: List<CheckboxNode> = emptyList()
) {
    val state: ToggleableState
        get() = when {
            children.isEmpty() -> if (isChecked) ToggleableState.On else ToggleableState.Off
            children.all { it.state == ToggleableState.On } -> ToggleableState.On
            children.all { it.state == ToggleableState.Off } -> ToggleableState.Off
            else -> ToggleableState.Indeterminate
        }

    fun toggle(): CheckboxNode {
        return copy(isChecked = !isChecked)
    }
}