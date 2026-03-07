package com.jeremiascortes.flowguide.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeremiascortes.flowguide.ui.theme.FlowGuideTheme

/**
 * A sample for a [MediumTopAppBar] that collapses when the content is scrolled up, and appears when
 * the content is completely scrolled back down.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCollapsableTopAppBar(
    title: @Composable () -> Unit,
    onBack: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lazyListState = rememberLazyListState()

    // Determinamos si la lista puede hacer scroll.
    // Si no puede hacer scroll, queremos que la TopAppBar esté colapsada para que el título no se vea "separado".
    val canScroll by remember {
        derivedStateOf {
            lazyListState.canScrollForward || lazyListState.canScrollBackward
        }
    }

    // Si no hay scroll posible, forzamos el colapso de la barra.
    // El offset de la altura se mueve entre 0 (expandido) y un valor negativo (colapsado).
    // Usamos LaunchedEffect para que cuando cambie la capacidad de scroll, se ajuste la barra.
    LaunchedEffect(canScroll) {
        if (!canScroll) {
            // Forzamos el offset al máximo colapso.
            // Nota: heightOffsetLimit suele ser un valor negativo (ej: -200.0f)
            scrollBehavior.state.heightOffset = scrollBehavior.state.heightOffsetLimit
        }
    }

    Scaffold(
        modifier = if (canScroll) {
            Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        } else {
            Modifier
        },
        topBar = {
            MediumTopAppBar(
                title = title,
                navigationIcon = {
                    TooltipBox(
                        positionProvider =
                            TooltipDefaults.rememberTooltipPositionProvider(
                                TooltipAnchorPosition.Above
                            ),
                        tooltip = { PlainTooltip { Text("Volver a la Pantalla Principal") } },
                        state = rememberTooltipState(),
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Menu")
                        }
                    }
                },
                actions = actions,
                scrollBehavior = scrollBehavior,
            )
        },
        content = { innerPadding ->
            LazyColumn(
                state = lazyListState,
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                content()
            }
        },
    )
}

@Preview
@Composable
fun AutoCollapsableTopAppBarPreview() {
    FlowGuideTheme {
        AutoCollapsableTopAppBar(
            title = { Text("Medium TopAppBar", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            content = {
                val list = (0..30).map { it.toString() }
                items(count = list.size) {
                    Text(
                        text = list[it],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        )
    }
}