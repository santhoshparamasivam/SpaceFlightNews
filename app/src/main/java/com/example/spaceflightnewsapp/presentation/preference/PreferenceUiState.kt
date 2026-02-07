package com.example.spaceflightnewsapp.presentation.preference

data class PreferenceUiState(
    val isLoading: Boolean = false,
    val sources: List<String> = emptyList(),
    val visibleSources: List<String> = emptyList(),
    val selected: String? = null,
    val searchQuery: String = "",
    val error: String? = null
)
