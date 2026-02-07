package com.example.spaceflightnewsapp.network

import com.example.spaceflightnewsapp.data.model.ArticlesResponse
import com.example.spaceflightnewsapp.data.model.InfoResponse
import retrofit2.http.GET
import retrofit2.http.Query
interface SpaceflightApi {

    @GET("v4/articles/")
    suspend fun getArticles(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("news_site") newsSite: String? = null
    ): ArticlesResponse

    @GET("v4/info/")
    suspend fun getInfo(): InfoResponse
}
