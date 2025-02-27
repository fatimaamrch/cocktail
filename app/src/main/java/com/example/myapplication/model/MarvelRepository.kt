package com.example.myapplication.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

// Classe de données pour la réponse JSON de l'API
data class CharacterResponse(
    val data: CharacterData
)

data class CharacterData(
    val results: List<Character>
)

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail
)

data class Thumbnail(
    val path: String,
    val extension: String
)

object MarvelRepository {

    // Client HTTP et Gson pour envoyer des requêtes et parser le JSON
    private val client = OkHttpClient()
    private val gson = Gson()

    // Méthode pour envoyer une requête GET
    private fun sendGet(url: String): String {
        val request = Request.Builder()
            .url(url) // URL de la requête
            .get()    // Utiliser GET pour la requête
            .addHeader("x-rapidapi-key", "8f4ddb5b81mshdd52639e1bcc6f5p18e117jsn286fbf9ca1ef")  // Clé API
            .addHeader("x-rapidapi-host", "marvelstefan-skliarovv1.p.rapidapi.com")  // Hôte de l'API
            .build()

        return client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Réponse du serveur incorrecte : ${response.code}\n${response.body.string()}")
            }
            response.body.string() ?: throw Exception("Réponse vide")
        }
    }

    // Méthode pour récupérer les personnages Marvel
    fun getMarvelCharacters(): List<Character> {
        val apiKey = "8f4ddb5b81mshdd52639e1bcc6f5p18e117jsn286fbf9ca1ef" // Remplacez par votre clé API
        val url = "https://gateway.marvel.com/v1/public/characters?apikey=$apiKey"

        // Effectuer la requête et obtenir la réponse JSON
        val jsonResponse = sendGet(url)

        // Parser la réponse JSON avec Gson
        val characterResponse = gson.fromJson(jsonResponse, CharacterResponse::class.java)

        // Retourner la liste des personnages
        return characterResponse.data.results
    }
}

// Utilisation dans la fonction main
fun main() {
    try {
        // Appel de l'API pour récupérer les personnages
        val characters = MarvelRepository.getMarvelCharacters()

        // Affichage des personnages récupérés
        characters.forEach { character ->
            println("Nom : ${character.name}")
            println("Description : ${character.description.take(100)}...")  // Afficher une partie de la description
            println("Image URL : ${character.thumbnail.path}.${character.thumbnail.extension}")
            println("----------------------------------------------------")
        }
    } catch (e: Exception) {
        println("Erreur lors de la récupération des données : ${e.message}")
    }
}
