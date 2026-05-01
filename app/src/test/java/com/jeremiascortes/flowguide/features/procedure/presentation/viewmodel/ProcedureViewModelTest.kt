package com.jeremiascortes.flowguide.features.procedure.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jeremiascortes.flowguide.MainDispatcherRule
import com.jeremiascortes.flowguide.features.procedure.domain.model.Procedure
import com.jeremiascortes.flowguide.features.procedure.domain.model.ProcedureState
import com.jeremiascortes.flowguide.features.procedure.domain.model.Step
import com.jeremiascortes.flowguide.features.procedure.domain.usecase.GetProcedureWithStepsUseCase
import com.jeremiascortes.flowguide.features.procedure.domain.usecase.UpdateStepCompletion
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProcedureViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProcedureWithStepsUseCase: GetProcedureWithStepsUseCase = mockk()
    private val toggleStepCompletionUseCase: UpdateStepCompletion = mockk(relaxed = true)

    private lateinit var viewModel: ProcedureViewModel

    companion object {
        private const val PROCEDURE_ID = "123"
        private val steps = listOf(
            Step(
                id = "1",
                name = "Paso 1",
                description = "Descripción del paso 1",
                isCompleted = false,
                order = 1,
                substeps = emptyList(),
            ),
            Step(
                id = "2",
                name = "Paso 2",
                description = "Descripción del paso 2",
                isCompleted = true,
                order = 2,
                substeps = emptyList(),
            ),
            Step(
                id = "3",
                name = "Paso 3",
                description = "Descripción del paso 3",
                isCompleted = false,
                order = 3,
                substeps = emptyList(),
            ),
        )
        private val procedure = Procedure(
            id = PROCEDURE_ID,
            name = "Procedimiento de prueba",
            description = "Descripción del procedimiento de prueba",
            isCompleted = false,
            steps = steps,
        )
    }

    @Before
    fun setup() {
        // Creamos SavedStateHandle vacío para que init no llame a loadProcedure automáticamente
        coEvery { getProcedureWithStepsUseCase(PROCEDURE_ID) }.returns(Result.success(procedure))

        viewModel = ProcedureViewModel(
            savedStateHandle = SavedStateHandle(),
            getProcedureWithStepsUseCase = getProcedureWithStepsUseCase,
            toggleStepCompletionUseCase = toggleStepCompletionUseCase
        )
    }

    @Test
    fun `loadProcedure exitoso emite estado inicial, loading y luego el procedure cargado`() =
        runTest {
            viewModel.state.test {
                assertEquals(ProcedureState(), awaitItem())

                viewModel.loadProcedure(PROCEDURE_ID)

                advanceUntilIdle()
                assertEquals(ProcedureState(isLoading = true), awaitItem())

                advanceUntilIdle()
                assertEquals(
                    ProcedureState(procedure = procedure, isLoading = false),
                    awaitItem()
                )

                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `loadProcedure fallido emite loading y luego error con isLoading false`() = runTest {
        coEvery { getProcedureWithStepsUseCase(PROCEDURE_ID) }.returns(
            Result.failure(Exception("Error al cargar el procedimiento"))
        )

        viewModel.state.test {
            assertEquals(ProcedureState(), awaitItem())

            viewModel.loadProcedure(PROCEDURE_ID)

            advanceUntilIdle()
            assertEquals(ProcedureState(isLoading = true), awaitItem())

            advanceUntilIdle()
            assertEquals(
                ProcedureState(
                    procedure = null,
                    isLoading = false,
                    error = "Error al cargar el procedimiento"
                ),
                awaitItem()
            )

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `init carga el procedimiento automaticamente cuando procedureId esta en SavedStateHandle`() =
        runTest {
            val savedStateHandle = SavedStateHandle(mapOf("procedureId" to PROCEDURE_ID))

            val viewModelWithId = ProcedureViewModel(
                savedStateHandle = savedStateHandle,
                getProcedureWithStepsUseCase = getProcedureWithStepsUseCase,
                toggleStepCompletionUseCase = toggleStepCompletionUseCase
            )

            advanceUntilIdle()

            assertEquals(procedure, viewModelWithId.state.value.procedure)
            assertEquals(false, viewModelWithId.state.value.isLoading)
        }

    @Test
    fun `toggleStepCompletion marca un paso como completado y llama al use case`() = runTest {
        viewModel.loadProcedure(PROCEDURE_ID)
        advanceUntilIdle()

        // Verificamos estado inicial: paso 1 no completado
        assertEquals(false, viewModel.state.value.procedure?.steps?.get(0)?.isCompleted)

        // Acción: marcamos el paso 1 como completado
        viewModel.toggleStepCompletion("1", true)
        advanceUntilIdle()

        // Verificación: el paso 1 ahora está completado y no hay loading residual
        assertEquals(true, viewModel.state.value.procedure?.steps?.get(0)?.isCompleted)
        assertEquals(false, viewModel.state.value.isLoading)

        // Verificamos que el use case fue llamado con los parámetros correctos
        coVerify { toggleStepCompletionUseCase("1", true) }
    }

    @Test
    fun `resetAllStepsCompletion pone todos los pasos a no completados y llama al use case por cada paso`() = runTest {
        viewModel.loadProcedure(PROCEDURE_ID)
        advanceUntilIdle()

        assertEquals(false, viewModel.state.value.procedure?.steps?.get(0)?.isCompleted)
        assertEquals(true, viewModel.state.value.procedure?.steps?.get(1)?.isCompleted)
        assertEquals(false, viewModel.state.value.procedure?.steps?.get(2)?.isCompleted)

        viewModel.resetAllStepsCompletion(false)
        advanceUntilIdle()

        assertEquals(false, viewModel.state.value.procedure?.steps?.get(0)?.isCompleted)
        assertEquals(false, viewModel.state.value.procedure?.steps?.get(1)?.isCompleted)
        assertEquals(false, viewModel.state.value.procedure?.steps?.get(2)?.isCompleted)

        coVerify(exactly = 3) { toggleStepCompletionUseCase(any(), eq(false)) }
    }
}
