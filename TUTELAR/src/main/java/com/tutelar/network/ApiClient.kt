package com.tutelar.network

import com.google.gson.GsonBuilder
import com.tutelar.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created at: Aug 2022
 * Created by: Gowtham R
 */
internal object ApiClient {

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(
            120,
            TimeUnit.SECONDS
        )
        .connectTimeout(120, TimeUnit.SECONDS)
        .build()

    val getClient: Api
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

            return retrofit.create(Api::class.java)
        }
}