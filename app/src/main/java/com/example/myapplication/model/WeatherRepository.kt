package com.example.myapplication.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

//Utilisation
fun main() {

    val res = WeatherRepository.loadWeathers("Nice")
    for(city in res){
        println("""
           Il fait ${city.main.temp}° à ${city.name} (id=${city.id}) avec un vent de ${city.wind.speed} m/s
           -Description : ${city.weather.getOrNull(0)?.description ?: "-"}
           -Icône : ${city.weather.getOrNull(0)?.icon ?: "-"}
       """.trimIndent())
    }


//    repeat(100) {
//        val user = WeatherRepository.loadRandomUser()
//        println(user)
//        println(
//            """
//        Il s'appelle ${user.name} pour le contacter :
//        Phone : ${user.coord?.phone ?: "-" }
//        Mail : ${user.coord?.mail ?: "-"}
//    """.trimIndent()
//        )
//    }

}


object WeatherRepository {
    val client = OkHttpClient()
    val gson = Gson()

    fun loadWeathers(cityname:String) : List<WeatherBean> {
        val apiKey = "b80967f0a6bd10d23e44848547b26550"
        val json :String = sendGet("https://api.openweathermap.org/data/2.5/find?q=$cityname&cnt=5&appid=$apiKey&units=metric&lang=fr")

        val res :WeatherAPIResultBean = gson.fromJson(json, WeatherAPIResultBean::class.java)

        res.list.forEach {
            it.weather.forEach {
                it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png"
            }
        }

        return res.list
    }


    fun loadRandomUser(): UserBean {
        val json :String = sendGet("https://www.amonteiro.fr/api/randomuser")
        val user :UserBean = gson.fromJson(json, UserBean::class.java)
        return user
    }

    //Méthode qui prend en entrée une url, execute la requête
    //Retourne le code HTML/JSON reçu
    fun sendGet(url: String): String {
        println("url : $url")
        //Création de la requête
        val request = Request.Builder().url(url).build()
        //Execution de la requête
        return client.newCall(request).execute().use { //it:Response
            //use permet de fermer la réponse qu'il y ait ou non une exception
            //Analyse du code retour
            if (!it.isSuccessful) {
                throw Exception("Réponse du serveur incorrect :${it.code}\n${it.body.string()}")
            }
            //Résultat de la requête
            it.body.string()
        }
    }
}

/* -------------------------------- */
// API WEATHER
/* -------------------------------- */
data class WeatherAPIResultBean(var list:List<WeatherBean>)
data class WeatherBean(var id:String, var name:String, var main:TempBean, var wind:WindBean, val weather:List<DescriptionBean>)
data class TempBean(var temp:Double)
data class WindBean(var speed:Double)
data class DescriptionBean(var description:String, var icon:String)



/* -------------------------------- */
// API USER
/* -------------------------------- */
data class UserBean(var name:String, var age:Int, var coord :CoordBean?  )
data class CoordBean(var phone:String?, var mail:String?)