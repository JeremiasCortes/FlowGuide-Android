package com.jeremiascortes.flowguide.presentation.components.BottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
* BottomBar reutilizable que recibe una lista de acciones configurables.
*
* @param items Lista de acciones a mostrar en el BottomBar
* @param fabItem Item opcional para el FloatingActionButton
* @param modifier Modifier para personalizar el contenedor
*/
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    fabItem: BottomBarItem? = null,
    items: List<BottomBarItem>,
) {
    BottomAppBar(
        modifier = modifier,
        actions = {
            items.forEach { item ->
                IconButton(
                    onClick = item.onClick,
                    enabled = item.enabled
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.contentDescription
                    )
                }
            }
        },
        floatingActionButton = fabItem?.let { fab ->
            {
                FloatingActionButton(
                    onClick = fab.onClick,
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(fab.icon, fab.contentDescription)
                }
            }
        }
    )
}