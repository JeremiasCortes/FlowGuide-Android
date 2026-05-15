package com.jeremiascortes.flowguide.features.home.presentation.viewmodel

import app.cash.turbine.test
import com.jeremiascortes.flowguide.features.home.data.model.SpaceDto
import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult
import com.jeremiascortes.flowguide.features.home.domain.model.HomeState
import com.jeremiascortes.flowguide.features.home.domain.usecase.GetAllFoldersByIdSpaceUseCase
import com.jeremiascortes.flowguide.features.home.domain.usecase.GetAllProceduresByIdFolderUseCase
import com.jeremiascortes.flowguide.features.home.domain.usecase.GetAllProceduresByIdSpaceUseCase
import com.jeremiascortes.flowguide.features.home.domain.usecase.GetAllSpacesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class HomeViewModelTest {

    private val getAllSpacesUseCase: GetAllSpacesUseCase = mockk()
    private val getAllFoldersByIdSpaceUseCase: GetAllFoldersByIdSpaceUseCase = mockk()
    private val getAllProceduresByIdSpaceUseCase: GetAllProceduresByIdSpaceUseCase = mockk()
    private val getAllProceduresByIdFolderUseCase: GetAllProceduresByIdFolderUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    companion object {
        val listOfSpaces = listOf(
            SpaceDto(
                idSpace = "id_space_1",
                title = "Escuela",
                userId = "user-123",
                createdAt = "2000-01-01T00:00:00Z",
                updatedAt = "2000-01-01T00:00:00Z"
            ),
            SpaceDto(
                idSpace = "id_space_2",
                title = "Trabajo",
                userId = "user-456",
                createdAt = "2000-01-01T00:00:00Z",
                updatedAt = "2000-01-01T00:00:00Z"
            ),
            SpaceDto(
                idSpace = "id_space_3",
                title = "Personal",
                userId = "user-789",
                createdAt = "2000-01-01T00:00:00Z",
                updatedAt = "2000-01-01T00:00:00Z"
            )
        )
    }

    @Before
    fun setup() {
        coEvery { getAllSpacesUseCase() }.returns(HomeResult.Success(listOfSpaces))
        coEvery { getAllFoldersByIdSpaceUseCase(any()) }.returns(HomeResult.Success(listOf()))
        coEvery { getAllProceduresByIdSpaceUseCase(any()) }.returns(HomeResult.Success(listOf()))
        coEvery { getAllProceduresByIdFolderUseCase(any()) }.returns(HomeResult.Success(listOf()))

        viewModel = HomeViewModel(
            getAllSpacesUseCase = getAllSpacesUseCase,
            getAllFoldersByIdSpaceUseCase = getAllFoldersByIdSpaceUseCase,
            getAllProceduresByIdSpaceUseCase = getAllProceduresByIdSpaceUseCase,
            getAllProceduresByIdFolderUseCase = getAllProceduresByIdFolderUseCase
        )
    }

    @Test
    fun `Cargas todos los espacios exitosamente y se autoselecciona automaticamente el primero`() =
        runTest {
            viewModel.state.test {
                assertEquals(
                    HomeState(
                        isLoading = false,
                        spaces = listOfSpaces,
                        selectedSpaceId = listOfSpaces.first().idSpace
                    ), awaitItem()
                )

                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `Se selecciona exitosamente un espacio`() = runTest {
        viewModel.selectSpace("id_space_2")
        viewModel.state.test {
            assertEquals(
                HomeState(
                    isLoading = false,
                    spaces = listOfSpaces,
                    selectedSpaceId = "id_space_2"
                ), awaitItem()
            )

            ensureAllEventsConsumed()
        }
    }

    // Este test 
    @Test
    fun `Se expande o contrae un espacio`() = runTest {
        viewModel.toggleFolder("id_space_3")

        viewModel.state.test {
            assertEquals(
                HomeState(
                    isLoading = false,
                    spaces = listOfSpaces,
                    selectedSpaceId = listOfSpaces.first().idSpace
                ),
                awaitItem()
            )

            advanceUntilIdle()
            assertEquals(
                HomeState(
                    isLoading = false,
                    spaces = listOfSpaces,
                    expandedFolderIds = setOf("id_space_3"),
                    selectedSpaceId = listOfSpaces.first().idSpace
                ),
                awaitItem()
            )

            ensureAllEventsConsumed()
        }
    }

}