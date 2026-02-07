package com.example.spaceflightnewsapp.presentation.discover

import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.data.model.NewsCategory

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val allArticles: List<Article> = emptyList(),
    val visibleArticles: List<Article> = emptyList(),
    val selectedCategory: NewsCategory = NewsCategory.ALL,
    val searchQuery: String = "",
    val error: String? = null
)