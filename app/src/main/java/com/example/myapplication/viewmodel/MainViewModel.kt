package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.PictureBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

const val LONG_TEXT = """Le Lorem Ipsum est simplement du faux texte employé dans la composition
    et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard
    de l'imprimerie depuis les années 1500"""

open class MainViewModel : ViewModel() {
    private val fullDataList = MutableStateFlow(emptyList<PictureBean>())
    private val _filteredDataList = MutableStateFlow(emptyList<PictureBean>())
    val filteredDataList: StateFlow<List<PictureBean>> get() = _filteredDataList

    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

//    fun loadWeathers(cityName: String) {
//        runInProgress.value = true
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val weatherList = WeatherRepository.loadWeathers(cityName)
//                val mappedList = weatherList.map { city ->
//                    PictureBean(
//                        id = city.id.toInt(),
//                        url = city.weather.firstOrNull()?.icon ?: "",
//                        title = city.name,
//                        longText = """
//                           Il fait ${city.main.temp}° à ${city.name} (id=${city.id}) avec un vent de ${city.wind.speed} m/s
//                           - Description : ${city.weather.getOrNull(0)?.description ?: "-"}
//                           - Icône : ${city.weather.getOrNull(0)?.icon ?: "-"}
//                       """.trimIndent()
//                    )
//                }
//                fullDataList.value = mappedList
//                _filteredDataList.value = mappedList
//            } catch (e: Exception) {
//                e.printStackTrace()
//                errorMessage.value = e.message ?: "Une erreur est survenue"
//            }
//            runInProgress.value = false
//        }
//    }

    init {
        loadFakeData()
    }

    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        val fakeData = listOf(
            PictureBean(1, "https://picsum.photos/200", "ABCD", LONG_TEXT),
            PictureBean(2, "https://picsum.photos/201", "BCDE", LONG_TEXT),
            PictureBean(3, "https://picsum.photos/202", "CDEF", LONG_TEXT),
            PictureBean(4, "https://picsum.photos/203", "EFGH", LONG_TEXT)
        ).shuffled()

        fullDataList.value = fakeData
        _filteredDataList.value = fakeData
    }

    fun search(query: String) {
        _filteredDataList.value = if (query.isBlank()) {
            fullDataList.value
        } else {
            fullDataList.value.filter { it.title.contains(query, ignoreCase = true) }
        }
    }

    fun clearFilter() {
        _filteredDataList.value = fullDataList.value
    }
}
