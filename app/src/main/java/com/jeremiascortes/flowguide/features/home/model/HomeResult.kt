package com.jeremiascortes.flowguide.features.home.model

sealed class HomeResult<out T> {
    data object Loading : HomeResult<Nothing>()
    data class Success<T>(val data: T) : HomeResult<T>()
    data class Error(val message: String) : HomeResult<Nothing>()
}