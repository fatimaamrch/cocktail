package com.example.myapplication.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

data class CocktailBean(val id: Int, val title: String, val difficulty: String, val image: String)

object CocktailRepository {
    private val client = OkHttpClient()
    private val gson = Gson()
    private const val API_URL = "https://the-cocktail-db3.p.rapidapi.com/"  // URL de base
    private const val API_KEY = "3f58d16fb6msh55e470dc672331ep174154jsn7d4b3926a3c9"  // Votre clé d'API

    fun loadCocktails(): List<CocktailBean> {
        val json: String = sendGet(API_URL)
        // Adaptez ici en récupérant directement la liste au lieu de l'objet englobant
        return gson.fromJson(json, Array<CocktailBean>::class.java).toList()
    }

    private fun sendGet(url: String): String {
        val request = Request.Builder()
            .url(url)
            .get()  // Effectuer la requête GET
            .addHeader("x-rapidapi-key", API_KEY)
            .addHeader("x-rapidapi-host", "the-cocktail-db3.p.rapidapi.com")
            .build()

        return client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Erreur HTTP : ${response.code}\n${response.body?.string()}")
            }
            response.body?.string() ?: throw Exception("Réponse vide du serveur")
        }
    }
}

// Fonction principale pour tester la récupération des cocktails
fun main() {
    try {
        val cocktails = CocktailRepository.loadCocktails()

        if (cocktails.isEmpty()) {
            println("Aucun cocktail trouvé.")
        } else {
            cocktails.forEach { cocktail ->
                println("Cocktail: ${cocktail.title}")
                println("Difficulté: ${cocktail.difficulty}")
                println("Image: ${cocktail.image}")
                println("-----------------------------")
            }
        }
    } catch (e: Exception) {
        println("Erreur : ${e.message}")
        e.printStackTrace()
    }
}
