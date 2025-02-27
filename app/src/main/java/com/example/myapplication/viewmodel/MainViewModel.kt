import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import com.example.myapplication.model.PictureBean

// ViewModel
class MainViewModel : ViewModel() {
    // MutableStateFlow pour stocker la liste de PictureBean
    val dataList = MutableStateFlow(emptyList<PictureBean>())
    val runInProgress = MutableStateFlow(false) // Indicateur de chargement
    val errorMessage = MutableStateFlow<String?>(null) // Message d'erreur

    fun loadWeathers(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runInProgress.value = true
            errorMessage.value = null // Réinitialiser le message d'erreur

            try {
                val weathers = WeatherRepository.loadWeathers(cityName)

                if (weathers != null && weathers.isNotEmpty()) {
                    // Transformation des données en PictureBean
                    dataList.value = weathers.map { weather ->
                        PictureBean(
                            id = weather.id,
                            url = weather.weather.firstOrNull()?.icon ?: "",
                            title = weather.name,
                            longText = "Il fait ${weather.main.temp}° à ${weather.name}."
                        )
                    }
                } else {
                    errorMessage.value = "Aucune donnée météo disponible pour la ville $cityName."
                    dataList.value = emptyList() // Réinitialiser la liste des données
                }
            } catch (e: Exception) {
                errorMessage.value = "Erreur lors de la récupération des données : ${e.message}"
                dataList.value = emptyList() // Réinitialiser la liste des données en cas d'erreur
            } finally {
                runInProgress.value = false
            }
        }
    }
}

// Main fonction pour tester le ViewModel
fun main() {
    val viewModel = MainViewModel()

    // Tester le main de MainViewModel
    runBlocking {
        // Lancer la récupération des données pour Toulouse
        viewModel.loadWeathers("Toulouse")

        // Attendre que les données soient chargées
        while (viewModel.runInProgress.value) {
            delay(500) // Attente avec une pause de 500ms pour ne pas bloquer le thread
        }

        // Afficher les données ou message d'erreur
        if (viewModel.errorMessage.value != null) {
            println("Erreur : ${viewModel.errorMessage.value}")
        } else if (viewModel.dataList.value.isNotEmpty()) {
            val firstWeather = viewModel.dataList.value[0]
            println("Il fait ${firstWeather.longText}")
        } else {
            println("Aucune donnée météo disponible.")
        }
    }
}
