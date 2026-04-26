package com.jeremiascortes.flowguide

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * ============================================================================
 * REGLA JUnit: MainDispatcherRule
 * ============================================================================
 *
 * Reemplaza Dispatchers.Main con un TestDispatcher durante los tests.
 *
 * PROBLEMA QUE RESUELVE:
 * viewModelScope.launch usa Dispatchers.Main internamente.
 * En un test unitario (JVM) NO existe Dispatchers.Main → explota.
 * Esta regla lo reemplaza con un dispatcher de test que ejecuta
 * las corrutinas de forma síncrona e instantánea.
 *
 * CÓMO FUNCIONA:
 * - @get:Rule → JUnit ejecuta starting() ANTES de cada @Test
 * - @get:Rule → JUnit ejecuta finished() DESPUÉS de cada @Test
 * - Así Dispatchers.Main está "mockeado" solo durante el test
 *
 * USO:
 * @get:Rule
 * val mainDispatcherRule = MainDispatcherRule()
 *
 * NOTA: Esta clase es estándar en Android. La usaremos en TODOS
 * los tests de ViewModel del proyecto.
 * ============================================================================
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    /**
     * Se ejecuta ANTES de cada test.
     * Reemplaza el dispatcher principal real por uno de test.
     */
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Se ejecuta DESPUÉS de cada test.
     * Restaura el dispatcher principal original para no afectar otros tests.
     */
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
