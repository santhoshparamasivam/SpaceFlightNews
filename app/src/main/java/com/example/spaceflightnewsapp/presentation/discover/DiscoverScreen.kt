package com.example.spaceflightnewsapp.presentation.discover

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.spaceflightnewsapp.data.preference.UserPreferenceDataStore
import com.example.spaceflightnewsapp.data.repository.NewsRepository
import com.example.spaceflightnewsapp.network.RetrofitInstance
import com.example.spaceflightnewsapp.presentation.components.ArticleCard
import com.example.spaceflightnewsapp.presentation.components.CategoryChips
import com.example.spaceflightnewsapp.presentation.components.EmptyState
import com.example.spaceflightnewsapp.presentation.components.ErrorState
import com.example.spaceflightnewsapp.presentation.components.LoadingState
import com.example.spaceflightnewsapp.presentation.components.SearchBar


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(navController: NavHostController) {

    val application = LocalContext.current.applicationContext

    val viewModel: DiscoverViewModel = viewModel {
        DiscoverViewModel(
            repository = NewsRepository(RetrofitInstance.api),
            prefs = UserPreferenceDataStore(application)
        )
    }

    val state by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { viewModel.refresh(fromUser = true) }
    )

    Scaffold(
        topBar = {
            TopAppBar(title = {  Column {
                Text(
                    text = "Discover",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "News from all around the world",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            } })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("preferences") }
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Preferences")
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .pullRefresh(pullRefreshState)
        ) {

            LazyColumn {

                item {
                    SearchBar(
                        query = state.searchQuery,
                        onQueryChange = viewModel::onSearch
                    )
                }

                item {
                    CategoryChips(
                        selected = state.selectedCategory,
                        onSelect = viewModel::onCategorySelected
                    )
                }

                when {
                    state.error != null -> {
                        item {
                            ErrorState(
                                message = state.error!!,
                                onRetry = viewModel::refresh
                            )
                        }
                    }

                    state.isLoading && state.visibleArticles.isEmpty() -> {
                        item { LoadingState() }
                    }

                    state.visibleArticles.isEmpty() -> {
                        item {
                            EmptyState(
                                if (state.searchQuery.isNotBlank())
                                    "No results for \"${state.searchQuery}\""
                                else
                                    "No news for ${state.selectedCategory.name}"
                            )
                        }
                    }

                    else -> {
                        itemsIndexed(state.visibleArticles) { index, article ->
                            ArticleCard(article) {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("articles", state.visibleArticles)
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("index", index)
                                navController.navigate("detail")
                            }

                            if (index == state.visibleArticles.lastIndex) {
                                LaunchedEffect(index) {
                                    viewModel.loadNextPage()
                                }
                            }
                        }

                        if (state.isLoading) {
                            item { LoadingState() }
                        }
                    }
                }
            }

            //  Pull-to-refresh indicator
            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
