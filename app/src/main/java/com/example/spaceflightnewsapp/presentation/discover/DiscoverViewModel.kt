package com.example.spaceflightnewsapp.presentation.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.data.model.NewsCategory
import com.example.spaceflightnewsapp.data.preference.UserPreferenceDataStore
import com.example.spaceflightnewsapp.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val repository: NewsRepository,
    private val prefs: UserPreferenceDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState

    private var offset = 0
    private val limit = 10
    private var canLoadMore = true
    private var currentSource: String? = null

    init {

        observePreference()
    }
    /**
     * Observe saved news source preference and reload articles when it changes
     */
    private fun observePreference() {
        viewModelScope.launch {
            prefs.selectedSource
                .distinctUntilChanged()
                .collect { source ->
                    currentSource = source
                    refresh(fromUser = false)
                }
        }
    }
    /**
     * Refresh articles list, optionally showing pull-to-refresh indicator
     */
    fun refresh(fromUser: Boolean = false) {
        offset = 0
        canLoadMore = true

        _uiState.value = _uiState.value.copy(
            isRefreshing = fromUser,
            error = null,
            allArticles = emptyList(),
            visibleArticles = emptyList()
        )

        loadNextPage()
    }
    /**
     * Load next page of articles based on current source and pagination state
     */
    fun loadNextPage() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val articles = repository.loadArticles(limit, offset, currentSource)

                offset += limit
                canLoadMore = articles.isNotEmpty()

                val merged = _uiState.value.allArticles + articles

                _uiState.value = _uiState.value.copy(
                    allArticles = merged,
                    visibleArticles = applyFilters(
                        merged,
                        _uiState.value.selectedCategory,
                        _uiState.value.searchQuery
                    ),
                    isLoading = false,
                    isRefreshing = false // always stop spinner
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    error = "Failed to load news"
                )
            }
        }
    }

    /**
     * Apply local category filter without triggering an API call
     */
    fun onCategorySelected(category: NewsCategory) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            visibleArticles = applyFilters(
                _uiState.value.allArticles,
                category,
                _uiState.value.searchQuery
            )
        )
    }
    /**
     * Apply local search filter on article titles
     */
    fun onSearch(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            visibleArticles = applyFilters(
                _uiState.value.allArticles,
                _uiState.value.selectedCategory,
                query
            )
        )
    }
    /**
     * Filter articles by selected category and search query
     */
    private fun applyFilters(
        list: List<Article>,
        category: NewsCategory,
        query: String
    ): List<Article> {
        return list
            .filter { article ->
                category == NewsCategory.ALL ||
                        category.keywords.any { keyword ->
                            article.title.contains(keyword, true) ||
                                    article.summary.contains(keyword, true)
                        }
            }
            .filter { article ->
                query.isBlank() ||
                        article.title.contains(query, true)
            }
    }


}
