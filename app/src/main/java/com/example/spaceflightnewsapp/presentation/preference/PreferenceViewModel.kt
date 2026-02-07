package com.example.spaceflightnewsapp.presentation.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceflightnewsapp.data.preference.UserPreferenceDataStore
import com.example.spaceflightnewsapp.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PreferenceViewModel(
    private val repository: NewsRepository,
    private val prefs: UserPreferenceDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(PreferenceUiState())
    val state: StateFlow<PreferenceUiState> = _state

    init {
        observeSavedPreference()
        loadSources()
    }

    /**
     * Read previously saved preference and keep it highlighted
     */
    private fun observeSavedPreference() {
        viewModelScope.launch {
            prefs.selectedSource.collect { saved ->
                _state.value = _state.value.copy(selected = saved)
            }
        }
    }

    /**
     * Load all sources once from API
     */
    private fun loadSources() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val sources = repository.fetchSources()
            _state.value = _state.value.copy(
                isLoading = false,
                sources = sources,
                visibleSources = sources
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = "Failed to load sources"
            )
        }
    }

    /**
     * Local search only (no API call)
     */
    fun onSearch(query: String) {
        val filtered = _state.value.sources.filter {
            it.contains(query, ignoreCase = true)
        }

        _state.value = _state.value.copy(
            searchQuery = query,
            visibleSources = filtered
        )
    }

    /**
     * Select a source (highlight only)
     */
    fun selectSource(source: String) {
        _state.value = _state.value.copy(selected = source)
    }

    /**
     * Persist selection in DataStore
     */
    fun savePreference() {
        viewModelScope.launch {
            prefs.saveSource(_state.value.selected)
        }
    }

    fun retry() {
        loadSources()
    }
}
