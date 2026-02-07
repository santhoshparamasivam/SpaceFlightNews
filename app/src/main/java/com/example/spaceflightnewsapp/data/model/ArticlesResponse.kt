package com.example.spaceflightnewsapp.data.model

@kotlinx.serialization.Serializable
data class ArticlesResponse(
    val results: List<Article>
)
@kotlinx.serialization.Serializable
data class Article(
    val id: Int,
    val title: String,
    val summary: String,
    val image_url: String,
    val news_site: String,
    val published_at: String,
    val authors: List<Author> = emptyList()
)

@kotlinx.serialization.Serializable
data class Author(
    val name: String
)