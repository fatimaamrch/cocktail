package com.example.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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

    val navHostController : NavHostController = rememberNavController()
    //viewModel() en dehors de NavHost lie à l'Activité donc partagé entre les écrans
    //viewModel() dans le NavHost lié à la stack d'écran. Une instance par stack d'écran
    val mainViewModel : MainViewModel = viewModel()

    //Import version avec Composable
    NavHost(navController = navHostController, startDestination = Routes.SearchRoute,
        modifier = modifier) {
        //Route 1 vers notre SearchScreen
        composable<Routes.SearchRoute> {

            //Si créé ici, il sera propre à cet instance de l'écran
            //val mainViewModel : MainViewModel = viewModel()

            //on peut passer le navHostController à un écran s'il déclenche des navigations
            SearchScreen( navHostController = navHostController, mainViewModel = mainViewModel )
        }

        //Route 2 vers un écran de détail
        composable<Routes.DetailRoute> {
            val detailRoute = it.toRoute<Routes.DetailRoute>()
            val pictureBean = mainViewModel.dataList.collectAsStateWithLifecycle().value.first { it.id == detailRoute.id }

            DetailScreen(pictureBean = pictureBean,
                navHostController = navHostController,
            )
        }
    }
}