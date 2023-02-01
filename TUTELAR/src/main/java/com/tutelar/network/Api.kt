package com.tutelar.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * Created at: Aug 2022
 * Created by: Gowtham R
 */
internal interface Api {

    @GET("properties/open/api-key/validate")
    suspend fun isValidApiKey(@Header("api-key") apiKey: String): Response<com.tutelar.model.Response>

}