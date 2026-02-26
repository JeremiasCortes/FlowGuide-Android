package com.jeremiascortes.flowguide.features.home.presentation.components.folder

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.jeremiascortes.flowguide.features.home.data.model.ProcedureDto


@Composable
fun ProcedureItem(procedure: ProcedureDto) {
    ListItem(
        headlineContent = { Text(procedure.title) },
        supportingContent = { Text(procedure.description ?: "") }
    )
}