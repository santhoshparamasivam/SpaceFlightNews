package com.example.spaceflightnewsapp.presentation.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.spaceflightnewsapp.data.model.Article
import com.example.spaceflightnewsapp.presentation.detail.ArticleDetailScreen
import com.example.spaceflightnewsapp.presentation.discover.DiscoverScreen
import com.example.spaceflightnewsapp.presentation.preference.PreferenceScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(navController, startDestination = "discover") {

        composable("discover") {
            DiscoverScreen(navController)
        }

        composable("preferences") {
            PreferenceScreen(
                onDone = { navController.popBackStack() }
            )
        }

        composable("detail") {
            val articles =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<List<Article>>("articles")

            val index =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Int>("index") ?: 0

            if (articles != null) {
                ArticleDetailScreen(
                    articles = articles,
                    startIndex = index,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
