package com.example.spaceflightnewsapp.data.repository

import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.network.SpaceflightApi
class NewsRepository(private val api: SpaceflightApi) {

    suspend fun loadArticles(
        limit: Int,
        offset: Int,
        source: String?
    ): List<Article> {
        return if (source.isNullOrBlank()) {
            api.getArticles(limit, offset).results
        } else {
            api.getArticles(limit, offset, source).results
        }
    }

    suspend fun fetchSources(): List<String> {
        return api.getInfo().news_sites.sorted()
    }
}
