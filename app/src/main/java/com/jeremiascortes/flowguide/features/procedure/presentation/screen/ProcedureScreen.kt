package com.jeremiascortes.flowguide.features.procedure.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremiascortes.flowguide.features.procedure.domain.model.CheckboxNode
import com.jeremiascortes.flowguide.features.procedure.presentation.components.TreeCheckbox
import com.jeremiascortes.flowguide.features.procedure.presentation.viewmodel.ProcedureViewModel
import com.jeremiascortes.flowguide.presentation.components.AutoCollapsableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcedureScreen(
    procedureViewModel: ProcedureViewModel,
    onBack: () -> Unit
) {
    val state = procedureViewModel.state.collectAsState()
    val stepsList = state.value.procedure?.steps ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.value.procedure?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Tus acciones aquí (reset, etc.)
                    IconButton(onClick = { procedureViewModel.resetAllStepsCompletion() }) {
                        Icon(Icons.Default.RestartAlt, contentDescription = "Reset")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            onNodeClick = { _, newValue ->
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
}
