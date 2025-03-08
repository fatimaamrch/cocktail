package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.CocktailBeanDetails
import com.example.myapplication.model.CocktailRepository
import com.example.myapplication.model.PictureBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

const val LONG_TEXT = """Le Lorem Ipsum est simplement du faux texte employé dans la composition
    et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard
    de l'imprimerie depuis les années 1500"""

open class MainViewModel : ViewModel() {
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")
    val selectedCocktail = MutableStateFlow<PictureBean?>(null)
    val selectedCocktailDetails = MutableStateFlow<CocktailBeanDetails?>(null)

    init {
        loadCocktails("")
    }

    fun loadCocktails(cocktailName: String) {
        runInProgress.value = true
        errorMessage.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cocktailsList = CocktailRepository.loadCocktails(cocktailName)
                dataList.value = cocktailsList.map { cocktail ->
                    PictureBean(
                        id = cocktail.id,
                        url = cocktail.image,
                        title = cocktail.title,
                        longText = cocktail.difficulty
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"
            } finally {
                runInProgress.value = false
            }
        }
    }

    fun loadCocktailDetails(id: Int) {
        runInProgress.value = true
        errorMessage.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cocktail = CocktailRepository.loadCocktailDetails(id)
                selectedCocktailDetails.value = cocktail
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"
            } finally {
                runInProgress.value = false
            }
        }
    }

    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        dataList.value = listOf(
            PictureBean(1, "https://picsum.photos/200", "Cocktail 1", LONG_TEXT),
            PictureBean(2, "https://picsum.photos/201", "Cocktail 2", LONG_TEXT),
            PictureBean(3, "https://picsum.photos/202", "Cocktail 3", LONG_TEXT),
            PictureBean(4, "https://picsum.photos/203", "Cocktail 4", LONG_TEXT)
        ).shuffled()
    }
}