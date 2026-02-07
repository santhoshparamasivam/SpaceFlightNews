package com.example.spaceflightnewsapp.presentation.preference

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spaceflightnewsapp.data.preference.UserPreferenceDataStore
import com.example.spaceflightnewsapp.data.repository.NewsRepository
import com.example.spaceflightnewsapp.network.RetrofitInstance
import com.example.spaceflightnewsapp.presentation.components.EmptyState
import com.example.spaceflightnewsapp.presentation.components.ErrorState
import com.example.spaceflightnewsapp.presentation.components.SourceItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceScreen(
    onDone: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: PreferenceViewModel = viewModel {
        PreferenceViewModel(
            repository = NewsRepository(RetrofitInstance.api),
            prefs = UserPreferenceDataStore(context)
        )
    }

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preferences") },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // 🔍 Search bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearch,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search sources") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    ErrorState(
                        message = state.error!!,
                        onRetry = viewModel::retry
                    )
                }

                state.visibleSources.isEmpty() -> {
                    EmptyState("No sources found")
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.visibleSources) { source ->
                            SourceItem(
                                name = source,
                                selected = source == state.selected,
                                onClick = { viewModel.selectSource(source) }
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.savePreference()
                            onDone()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.selected != null
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
