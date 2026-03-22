package com.jeremiascortes.flowguide.features.settings.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.jeremiascortes.flowguide.features.auth.presentation.AuthViewModel
import com.jeremiascortes.flowguide.features.auth.presentation.NavigationEvent
import com.jeremiascortes.flowguide.features.settings.presentation.viewmodel.SettingsViewModel
import com.jeremiascortes.flowguide.presentation.components.AutoCollapsableTopAppBar
import com.jeremiascortes.flowguide.presentation.components.LoadingIndicator

@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    val state = settingsViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.ToLogin -> onLogout()
                is NavigationEvent.ToHome -> { /* no aplica aquí */ }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AutoCollapsableTopAppBar(
            title = { Text(text = "Settings") },
            onBack = onBack,
        ) {
            item {
                Button(onClick = { authViewModel.logout() }) {
                    Text(text = if (state.value.isLoading) "Cerrando sesión..." else "Cerrar Sesión")
                }
            }
            items(40) {
                Text(text = "Item $it")
            }
        }

        LoadingIndicator(isLoading = state.value.isLoading)
    }
}