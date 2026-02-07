package com.example.spaceflightnewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.spaceflightnewsapp.data.preference.UserPreferenceDataStore
import com.example.spaceflightnewsapp.presentation.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserPreferenceDataStore(applicationContext)
        setContent {
            val navController = rememberNavController()

            AppNavGraph(navController = navController)
        }
    }
}