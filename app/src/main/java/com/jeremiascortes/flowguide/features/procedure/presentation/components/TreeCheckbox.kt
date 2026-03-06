package com.jeremiascortes.flowguide.features.procedure.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.jeremiascortes.flowguide.features.procedure.domain.model.CheckboxNode

@Composable
fun TreeCheckbox(
    node: CheckboxNode,
    onNodeClick: (id: Int, newValue: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    level: Int = 0
) {
    Column(modifier = modifier.padding(start = (level * 24).dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .triStateToggleable(
                    state = node.state,
                    onClick = { onNodeClick(node.id, node.state != ToggleableState.On) },
                    role = Role.Checkbox
                )
                .padding(8.dp)
        ) {
            TriStateCheckbox(state = node.state, onClick = null)
            Spacer(Modifier.width(8.dp))
            Text(node.label)
        }

        node.children.forEach { child ->
            TreeCheckbox(
                node = child,
                onNodeClick = onNodeClick,
                level = level + 1
            )
        }
    }
}
