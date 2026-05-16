package com.jeremiascortes.flowguide.features.home.data.repository

import com.jeremiascortes.flowguide.features.auth.di.SupabaseClient
import com.jeremiascortes.flowguide.features.home.data.model.FolderDto
import com.jeremiascortes.flowguide.features.home.data.model.ProcedureDto
import com.jeremiascortes.flowguide.features.home.data.model.SpaceDto
import com.jeremiascortes.flowguide.features.home.data.model.StepDto
import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult
import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult.Success
import com.jeremiascortes.flowguide.features.home.domain.repository.HomeRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Implementación de la interfaz `HomeRepository` que proporciona métodos
 * para interactuar con las diferentes entidades en la base de datos de Supabase.
 *
 * Este repositorio utiliza `SupabaseClient` para ejecutar operaciones de backend,
 * como la obtención de datos y el cierre de sesión, mediante las tablas
 * definidas en la base de datos.
 *
 * Los datos recuperados de la base de datos se convierten a objetos DTO para ser
 * utilizados en las capas superiores de la aplicación.
 */
@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : HomeRepository {

    /**
     * Contiene constantes relacionadas con las tablas, columnas y metadata
     * del sistema de gestión de datos en Supabase para la capa de `HomeRepositoryImpl`.
     * Esta clase define los nombres de tablas y columnas para facilitar el acceso a la base de datos.
     *
     * Utiliza las siguientes convenciones:
     * - Los nombres de las tablas se definen como constantes en mayúsculas.
     * - Los nombres de las columnas siguen el patrón snake_case.
     * - Proporciona una estructura centralizada para evitar errores tipográficos y simplificar mantenimiento.
     *
     * Tablas en Supabase:
     * - `spaces`: Tabla que almacena espacios de usuario.
     * - `folders`: Tabla que almacena carpetas asociadas con espacios.
     * - `procedures`: Tabla que almacena procedimientos creados por los usuarios.
     * - `steps`: Tabla que almacena los pasos asociados a un procedimiento.
     *
     * Columnas comunes:
     * - `id_space`: Identificador único para la tabla de espacios.
     * - `id_folder`: Identificador único para la tabla de carpetas.
     * - `id_procedure`: Identificador único para la tabla de procedimientos.
     */
    companion object {
        /**
         * Nombre de la tabla "spaces" en la base de datos de Supabase.
         *
         * Esta constante se utiliza para interactuar con la tabla que almacena
         * información sobre los espacios en la plataforma. La tabla contiene
         * datos relacionados con los espacios creados por los usuarios, como
         * su identificador, título, usuario asociado, y marcas de tiempo de
         * creación y última actualización.
         *
         * Uso principal:
         * - Componente clave para realizar consultas CRUD sobre la tabla "spaces"
         *   a través del cliente de Supabase.
         *
         * Relación:
         * - Asociada a la estructura de datos definida en `SpaceDto`.
         */
// Nombres de las tablas en la BD de Supabase
        private const val TABLE_SPACES = "spaces"
        /**
         * Constante que representa el nombre de la tabla "folders" en la base de datos.
         *
         * Se utiliza para realizar operaciones relacionadas con las carpetas almacenadas en Supabase.
         * Generalmente es empleada como referencia en consultas para obtener, insertar,
         * actualizar o eliminar datos de la tabla correspondiente.
         */
        private const val TABLE_FOLDERS = "folders"
        /**
         * Nombre de la tabla en la base de datos Supabase que almacena los procedimientos.
         *
         * Este valor se utiliza como referencia para realizar consultas SQL en la tabla
         * correspondiente dentro del cliente `SupabaseClient`.
         *
         * Uso dentro de la clase:
         * - Especifica el nombre de la tabla al interactuar con la base de datos para realizar
         *   operaciones CRUD relacionadas con procedimientos.
         *
         * Propósito:
         * - Centralizar y evitar el uso de valores "hardcoded" para el nombre de la tabla.
         */
        private const val TABLE_PROCEDURES = "procedures"
        /**
         * Nombre de la tabla en la base de datos que representa los pasos (steps)
         * asociados a un procedimiento específico en la aplicación.
         *
         * Esta constante se utiliza como identificador para realizar consultas
         * a la base de datos, específicamente a la tabla que almacena los datos
         * relacionados con los pasos de un procedimiento.
         *
         * Asociación común:
         * - Cada paso está vinculado a un procedimiento mediante un campo de identificador
         *   de procedimiento (procedureId).
         *
         * Uso típico:
         * - Configuración de consultas en el cliente Supabase para operaciones
         *   relacionadas con los pasos, como obtenerlos, insertarlos o actualizarlos.
         */
        private const val TABLE_STEPS = "steps"

        /**
         * Nombre de la columna utilizada para identificar de forma única un espacio en la base de datos.
         * Esta constante es usada en consultas a la base de datos, especialmente al filtrar registros
         * relacionados con espacios específicos.
         *
         * Propósito:
         * - Representa la clave 'id_space' dentro de la tabla correspondiente en la base de datos.
         *
         * Contexto:
         * - Se utiliza principalmente en operaciones que requieren obtener carpetas, procedimientos
         *   o elementos asociados a un espacio determinado, por ejemplo, en la implementación de
         *   la función `getAllFoldersByIdSpace` en el repositorio `HomeRepositoryImpl`.
         *
         * Importancia:
         * - Permite mantener consistencia en el acceso a los datos al evitar el uso de cadenas de texto
         *   literal en diferentes partes del código.
         */
// Constantes para nombres de columnas
        private const val COLUMN_SPACE_ID = "space_id"
        /**
         * Nombre de la columna utilizada para identificar de manera única las carpetas en la base de datos.
         *
         * Este valor corresponde al campo 'id_folder' en la tabla de la base de datos asociada.
         * Se usa principalmente para aplicar filtros en las consultas relacionadas con carpetas,
         * como recuperar todos los procedimientos asociados a una carpeta específica.
         */
        private const val COLUMN_FOLDER_ID = "folder_id"
        /**
         * Nombre de la columna en la base de datos utilizada para identificar el procedimiento al que
         * pertenece un elemento (por ejemplo, un paso de un procedimiento).
         *
         * Esta constante es empleada como referencia en consultas a la base de datos, facilitando la
         * escritura de filtros y condiciones en operaciones realizadas con el cliente Supabase.
         *
         * Ejemplo de uso: La constante se utiliza para construir consultas donde se necesita filtrar
         * pasos según el procedimiento al que están asociados.
         */
        private const val COLUMN_PROCEDURE_ID = "id_procedure"
    }

    /**
     * Cierra la sesión del usuario actual en el sistema.
     *
     * Este método utiliza el cliente de Supabase para realizar la operación de
     * cierre de sesión. En caso de éxito, devuelve un resultado exitoso. Si ocurre
     * algún error durante el proceso, devuelve un resultado con un mensaje de error.
     *
     * @return HomeResult<Unit> Resultado del cierre de sesión, el cual puede ser:
     * - Success: Indica que el cierre de sesión se realizó correctamente.
     * - Error: Contiene un mensaje explicando el motivo del fallo.
     */
    override suspend fun logout(): HomeResult<Unit> {
        return try {
            supabase.supabaseClient.auth.signOut()
            Success(Unit)
        } catch (e: Exception) {
            HomeResult.Error("Error al cerrar sesión: ${e.message ?: "Error desconocido"}")
        }
    }

    /**
     * Recupera todas las entradas de la tabla de espacios desde la base de datos de Supabase.
     *
     * @return Un objeto de tipo [HomeResult] que puede contener:
     * - Una lista de objetos [SpaceDto] en caso de éxito.
     * - Un mensaje de error en caso de fallo.
     */
    override suspend fun getAllSpaces(): HomeResult<List<SpaceDto>> {
        return try {
            val spaces = supabase.supabaseClient
                .from(TABLE_SPACES) // Nombre de la tabla en la BD de Supabase
                .select() // Selecciona todas las columnas
                .decodeList<SpaceDto>() // Decodifica la respuesta en una lista de objetos SpaceDto

            // Devolvemos la lista de espacios
            Success(spaces)
        } catch (e: Exception) {
            HomeResult.Error("Error al obtener espacios: ${e.message ?: "Error desconocido"}")
        }
    }

    /**
     * Obtiene todas las carpetas asociadas al espacio dado por su ID.
     *
     * @param idSpace El identificador único del espacio para el cual se deben obtener las carpetas.
     * @return Un objeto de tipo [HomeResult] que puede ser:
     * - [HomeResult.Success] con una lista de objetos [FolderDto] si las carpetas fueron recuperadas con éxito.
     * - [HomeResult.Error] con un mensaje de error si ocurre alguna excepción durante la ejecución.
     */
    override suspend fun getAllFoldersByIdSpace(idSpace: String): HomeResult<List<FolderDto>> {
        return try {
            val folders = supabase.supabaseClient
                .from(TABLE_FOLDERS)
                .select() {
                    filter {
                        eq(COLUMN_SPACE_ID, idSpace)
                    }
                }
                .decodeList<FolderDto>()

            Success(folders)
        } catch (e: Exception) {
            HomeResult.Error("Error al obtener carpetas: ${e.message ?: "Error desconocido"}")
        }
    }

    override suspend fun getAllProceduresByIdSpace(idSpace: String): HomeResult<List<ProcedureDto>> {
        return try {
            val procedures = supabase.supabaseClient
                .from(TABLE_PROCEDURES)
                .select() {
                    filter {
                        eq(COLUMN_SPACE_ID, idSpace)
                    }
                }
                .decodeList<ProcedureDto>()

            Success(procedures)
        } catch (e: Exception) {
            HomeResult.Error("Error al obtener procedimientos: ${e.message ?: "Error desconocido"}")
        }
    }

    /**
     * Recupera una lista de procedimientos asociados a un folder específico identificado por su ID.
     *
     * @param idFolder Identificador único del folder del cual se quieren obtener los procedimientos.
     * @return Un objeto de tipo [HomeResult] que contiene una lista de [ProcedureDto] en caso
     * de éxito, o un mensaje de error en caso contrario.
     */
    override suspend fun getAllProceduresByIdFolder(idFolder: String): HomeResult<List<ProcedureDto>> {
        return try {
            val procedures = supabase.supabaseClient
                .from(TABLE_PROCEDURES)
                .select() {
                    filter {
                        eq(COLUMN_FOLDER_ID, idFolder)
                    }
                }
                .decodeList<ProcedureDto>()

            Success(procedures)
        } catch (e: Exception) {
            HomeResult.Error("Error al obtener procedimientos: ${e.message ?: "Error desconocido"}")
        }
    }

    /**
     * Recupera todos los pasos asociados a un procedimiento específico basado en su ID.
     *
     * @param idProcedure El identificador único del procedimiento para el cual se desean obtener los pasos.
     * @return Un resultado de tipo `HomeResult` que contiene una lista de objetos `StepDto` si la operación es exitosa,
     *         o un mensaje de error en caso contrario.
     */
    override suspend fun getAllStepsByIdProcedure(idProcedure: String): HomeResult<List<StepDto>> {
        return try {
            val steps = supabase.supabaseClient
                .from(TABLE_STEPS)
                .select() {
                    filter {
                        eq(COLUMN_PROCEDURE_ID, idProcedure)
                    }
                }
                .decodeList<StepDto>()

            Success(steps)
        } catch (e: Exception) {
            HomeResult.Error("Error al obtener pasos: ${e.message ?: "Error desconocido"}")
        }
    }

    override suspend fun createSpace(nameSpace: String): HomeResult<Unit> {
        return try{
            supabase.supabaseClient
                .from(TABLE_SPACES)
                .insert(SpaceDto(
                    title = nameSpace,
                    userId = supabase.supabaseClient.auth.currentUserOrNull()?.id ?: ""
                ))
            Success(Unit)
        } catch (e: Exception) {
            HomeResult.Error("Error al crear espacio: ${e.message ?: "Error desconocido"}")
        }
    }

    override suspend fun updateSpace(spaceDto: SpaceDto): HomeResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpace(spaceDto: SpaceDto): HomeResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun createFolder(nameFolder: String, spaceId: String): HomeResult<Unit> {
        return try{
            supabase.supabaseClient
                .from(TABLE_FOLDERS)
                .insert(FolderDto(
                    title = nameFolder,
                    userId = supabase.supabaseClient.auth.currentUserOrNull()?.id ?: "",
                    spaceId = spaceId
                ))
            Success(Unit)
        } catch (e: Exception) {
            HomeResult.Error("Error al crear carpeta: ${e.message ?: "Error desconocido"}")
        }
    }
}