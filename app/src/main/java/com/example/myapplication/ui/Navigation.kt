package com.example.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.DetailScreen
import com.example.myapplication.ui.screens.SearchScreen
import com.example.myapplication.viewmodel.MainViewModel
import kotlinx.serialization.Serializable

class Routes {
    @Serializable
    data object SearchRoute

    //les paramètres ne peuvent être que des types de base(String, Int, Double...)
    @Serializable
    data class DetailRoute(val id: Int)
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navHostController: NavHostController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()

    NavHost(navController = navHostController, startDestination = "search", modifier = modifier) {
        composable("search") {
            SearchScreen(navHostController = navHostController, mainViewModel = mainViewModel)
        }

        composable("detail/{cocktailId}") { backStackEntry ->
            val cocktailId = backStackEntry.arguments?.getString("cocktailId")?.toInt() ?: 0
            DetailScreen(
                navHostController = navHostController,
                mainViewModel = mainViewModel,
                cocktailId = cocktailId
            )
        }
    }
}