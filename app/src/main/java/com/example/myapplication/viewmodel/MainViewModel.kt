package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.CocktailRepository
import com.example.myapplication.model.PictureBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun main() {
    val viewModel = MainViewModel()
    viewModel.loadCocktails("")

    while (viewModel.runInProgress.value) {
        println("Attente...")
        Thread.sleep(500)
    }

    // Affichage de la liste (qui doit être remplie) contenue dans la donnée observable
    println("List : ${viewModel.dataList.value}")
    println("ErrorMessage : ${viewModel.errorMessage.value}")
}

const val LONG_TEXT = """Le Lorem Ipsum est simplement du faux texte employé dans la composition
    et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard
    de l'imprimerie depuis les années 1500"""

open class MainViewModel : ViewModel() {
    // MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    init {
        loadCocktails("")
    }

    fun loadCocktails(cocktailName: String) {
        runInProgress.value = true
        errorMessage.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Appel de la méthode loadCocktails() de CocktailRepository
                val cocktailsList = CocktailRepository.loadCocktails()
                // Mise à jour de la donnée observable avec les résultats
                dataList.value = cocktailsList.map { cocktail ->
                    PictureBean(
                        id = cocktail.id,
                        title = cocktail.title,
                        difficulty = cocktail.difficulty,
                        image = cocktail.image
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"
            }

            runInProgress.value = false
        }

        println("fin")
    }

    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        dataList.value = listOf(
            PictureBean(1, "Cocktail 1", "Facile", "https://picsum.photos/200"),
            PictureBean(2, "Cocktail 2", "Moyenne", "https://picsum.photos/201"),
            PictureBean(3, "Cocktail 3", "Difficile", "https://picsum.photos/202"),
            PictureBean(4, "Cocktail 4", "Facile", "https://picsum.photos/203")
        ).shuffled() // shuffled() pour avoir un ordre différent à chaque appel
    }
}