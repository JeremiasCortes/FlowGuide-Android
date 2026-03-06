package com.jeremiascortes.flowguide.features.procedure.domain.model

sealed class ProcedureResult<out T> {
    data object Loading : ProcedureResult<Nothing>()
    data class Success<T>(val data: T) : ProcedureResult<T>()
    data class Error(val message: String) : ProcedureResult<Nothing>()
}