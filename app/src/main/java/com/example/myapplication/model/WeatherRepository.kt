package com.example.myapplication.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.nio.charset.Charset

object WeatherRepository {
    val client = OkHttpClient()
    val gson = Gson()

    // Méthode qui prend en entrée une URL, exécute la requête et retourne le code HTML/JSON reçu
    fun sendGet(url: String): String {
        println("URL : $url")
        val request = Request.Builder().url(url).build()
        return client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Réponse du serveur incorrecte : ${response.code}\n${response.body.string()}")
            }
            response.body.string() ?: throw Exception("Réponse vide")
        }
    }

    // Méthode pour récupérer la météo d'une ville et retourner la liste des informations nécessaires
    fun loadWeathers(cityName: String): List<CityWeather>? {
        Thread.sleep(2000) //Ne pas inclure Thread.sleep(2000) après le TP
        val apiKey = "b80967f0a6bd10d23e44848547b26550"
        val url = "https://api.openweathermap.org/data/2.5/find?q=$cityName&cnt=5&appid=$apiKey&units=metric&lang=fr"
        val json = sendGet(url)

        // Parsing du JSON
        val response = gson.fromJson(json, WeatherResponse::class.java)

        return response.list.onEach { city ->
            city.weather.forEach { it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png" }
        }
    }


    // Méthode pour récupérer un utilisateur aléatoire
    fun loadRandomUser(): User? {
        val url = "https://www.amonteiro.fr/api/randomuser"
        val json = sendGet(url)

        // Parsing du JSON avec le bon modèle
        return gson.fromJson(json, User::class.java)
    }

}

// Utilisation dans le main
fun main() {
    System.setProperty("file.encoding", "UTF-8") // Force l'encodage
    println(Charset.defaultCharset()) // Vérifie si UTF-8 est bien utilisé
    // Récupération des données météorologiques de Toulouse
    val weathers: List<CityWeather>? = WeatherRepository.loadWeathers("Toulouse")

    if (weathers != null && weathers.isNotEmpty()) {
        val firstWeather = weathers[0]
        val description = firstWeather.weather.firstOrNull()?.description ?: "Non disponible"
        val icon = firstWeather.weather.firstOrNull()?.icon ?: "Non disponible"

        println("Il fait ${firstWeather.main.temp}° à ${firstWeather.name} (id=${firstWeather.id}) avec un vent de ${firstWeather.wind.speed} m/s")
        println("-Description : $description")
        println("-Icône : $icon")
    } else {
        println("Aucune donnée météo disponible.")
    }

    // Test avec RandomUser API
    val user: User? = WeatherRepository.loadRandomUser()
    if (user != null) {
        println("Il s'appelle ${user.name} et a ${user.age} ans. Contact :")

        // Vérifier si coord n'est pas null
        val phone = user.coord?.phone ?: "-"
        val mail = user.coord?.mail ?: "-"

        println("Phone : $phone")
        println("Mail : $mail")
    } else {
        println("Aucun utilisateur trouvé.")
    }
}

// Weather Data Classes
// Représente la réponse de l'API météo
data class WeatherResponse(
    val list: List<CityWeather>
)

// Représente les informations météorologiques d'une ville
data class CityWeather(
    val id: Int,
    val name: String,
    val main: MainWeather,
    val wind: Wind,
    val weather: List<WeatherDescription>
)

// Représente les informations de la température (température, pression, etc.)
data class MainWeather(
    val temp: Double
)

// Représente les informations sur le vent
data class Wind(
    val speed: Double
)

// Représente la description de la météo (ex : "couvert")
data class WeatherDescription(
    val description: String,
    var icon: String
)

// User Data Classes
// Représente la réponse de l'API randomuser
data class RandomUserResponse(val results: List<User>)

// Représente un utilisateur
data class User(
    val name: String, // "Bertrand"
    val age: Int, // 24
    val coord: Coord // Contient phone et mail
)

// Représente les coordonnées de l'utilisateur (numéro et email)
data class Coord(
    val phone: String?,
    val mail: String?
)
