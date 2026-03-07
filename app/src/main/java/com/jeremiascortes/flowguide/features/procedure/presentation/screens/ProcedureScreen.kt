package com.jeremiascortes.flowguide.features.procedure.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremiascortes.flowguide.features.procedure.domain.model.CheckboxNode
import com.jeremiascortes.flowguide.features.procedure.presentation.components.TreeCheckbox
import com.jeremiascortes.flowguide.features.procedure.presentation.viewmodel.ProcedureViewModel
import com.jeremiascortes.flowguide.ui.components.AutoCollapsableTopAppBar

@Composable
fun ProcedureScreen(
    procedureViewModel: ProcedureViewModel,
    onBack: () -> Unit
) {
    val state = procedureViewModel.state.collectAsState()
    val stepsList = state.value.procedure?.steps ?: emptyList()

    AutoCollapsableTopAppBar(
        title = {
            Text(state.value.procedure?.name ?: "")
        },
        onBack = onBack
    ) {
        itemsIndexed(stepsList) { index, step ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    val checkboxNode = remember(step) {
                        CheckboxNode(
                            id = index,
                            label = step.name,
                            isChecked = step.isCompleted,
                            idStep = step.id,
                            children = emptyList()
                        )
                    }

                    TreeCheckbox(
                        node = checkboxNode,
                        onNodeClick = { nodeId, newValue ->
                            procedureViewModel.toggleStepCompletion(step.id, newValue)
                        }
                    )

                    step.description?.let {
                        Text(
                            text = step.description,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
