package com.jeremiascortes.flowguide.features.settings.presentation.viewmodel

import app.cash.turbine.test
import com.jeremiascortes.flowguide.MainDispatcherRule
import com.jeremiascortes.flowguide.features.settings.domain.model.SettingsState
import com.jeremiascortes.flowguide.features.settings.domain.usecase.LogoutUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val logoutUseCase: LogoutUseCase = mockk()

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        viewModel = SettingsViewModel(
            logoutUseCase = logoutUseCase
        )
    }

    @Test
    fun `Estado inicial es SettingsState con isLoading false`() = runTest {
        assertEquals(SettingsState(isLoading = false), viewModel.state.value)
    }

    @Test
    fun `logout - exitoso, cambia isLoading a true y luego false`() = runTest {
        coEvery { logoutUseCase() }.returns(Result.success(Unit))

        viewModel.state.test {
            assertEquals(SettingsState(isLoading = false), awaitItem())

            viewModel.logout()
            assertEquals(SettingsState(isLoading = true), awaitItem())

            advanceUntilIdle()
            assertEquals(SettingsState(isLoading = false), awaitItem())

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `logout - fallido, cambia isLoading a true y luego false`() = runTest {
        coEvery { logoutUseCase() }.returns(Result.failure(Exception("Error de logout")))

        viewModel.state.test {
            assertEquals(SettingsState(isLoading = false), awaitItem())

            viewModel.logout()
            assertEquals(SettingsState(isLoading = true), awaitItem())

            advanceUntilIdle()
            assertEquals(SettingsState(isLoading = false), awaitItem())

            ensureAllEventsConsumed()
        }
    }
}
