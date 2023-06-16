package com.example.ziadartwork.ui.viewmodels

sealed class Result<out R> {
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T): Result<T>()
    data class Error<out T>(val exception: Exception?): Result<T>()
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null
