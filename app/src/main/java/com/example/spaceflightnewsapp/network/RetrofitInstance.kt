package com.example.spaceflightnewsapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
object RetrofitInstance {

    private const val BASE_URL = "https://api.spaceflightnewsapi.net/"

    private val json = Json { ignoreUnknownKeys = true }

    val api: SpaceflightApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(SpaceflightApi::class.java)
    }
}
