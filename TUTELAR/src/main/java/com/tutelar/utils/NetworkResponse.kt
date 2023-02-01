package com.tutelar.utils

/**
 * Created at Mar-2022
 * Get network status based on api response
 */
sealed class NetworkResponse<T>(val data: T? = null, val errorResponse: T? = null) {
    class Success<T>(data: T) : NetworkResponse<T>(data=data, null)
    class Error<T>() : NetworkResponse<T>(null, null)
    class Loading<T>() : NetworkResponse<T>()
    class NoInternet<T>() : NetworkResponse<T>()
    class ErrorResponse<T>(errorResponse: T) : NetworkResponse<T>(null, errorResponse = errorResponse)
}