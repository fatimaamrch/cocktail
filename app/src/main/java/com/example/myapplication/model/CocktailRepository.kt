package com.example.myapplication.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

data class CocktailBean(val id: Int, val title: String, val difficulty: String, val image: String)
data class CocktailBeanDetails(
    val id: Int,
    val title: String,
    val difficulty: String,
    val image: String,
    val portion: String,
    val time: String,
    val description: String,
    val ingredients: List<String>,
    val method: List<MethodStep>
)
data class MethodStep(
    val step: String
)


object CocktailRepository {
    private val client = OkHttpClient()
    private val gson = Gson()
    private const val API_URL = "https://the-cocktail-db3.p.rapidapi.com"  // URL de base
    private const val API_KEY = "4964012d3bmshe8d9f08118ef462p160e8fjsn4946341038bf"  // Votre clé d'API

    // Méthode pour charger les cocktails par nom
    fun loadCocktails(cocktailName: String): List<CocktailBean> {
        val url = API_URL
        val json: String = sendGet(url)
        return gson.fromJson(json, Array<CocktailBean>::class.java).toList()
    }

    // Méthode pour charger les détails d'un cocktail par ID
    fun loadCocktailDetails(id: Int): CocktailBeanDetails  {
        val url = "$API_URL/$id"
        val json: String = sendGet(url)
        return gson.fromJson(json, CocktailBeanDetails ::class.java)
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
//    val cocktails = CocktailRepository.loadCocktails("sangria")
//    for (cocktail in cocktails) {
//        println("""
//            🎮 Titre: ${cocktail.title}
//            🥵 Difficulté: ${cocktail.difficulty}
//            image: ${cocktail.image}
//        """.trimIndent())
//    }

    val cocktailDetails = CocktailRepository.loadCocktailDetails(45)
    println("""
        🎮 Titre: ${cocktailDetails.title}
        🧐 Description: ${cocktailDetails.description}
        🥵 Difficulté: ${cocktailDetails.difficulty}
        ⌚ Temps de préapration: ${cocktailDetails.time}
        🥖 Ingrédients: ${cocktailDetails.ingredients.joinToString(", ")}
        image: ${cocktailDetails.image}
    """.trimIndent())
}

//        🧑‍🍳 Méthode: ${cocktailDetails.method.joinToString("\n") { it.step }}