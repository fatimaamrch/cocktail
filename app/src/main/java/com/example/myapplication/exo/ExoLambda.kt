package com.example.myapplication.exo

// Définition de la classe UserBean
data class UserBean(val name: String, val old: Int)

fun main() {
    exo1()
    exo2()
}

fun exo1() {
    // Déclaration des lambdas
    val lower: (String) -> Unit = { text -> println(text.lowercase()) }

    val hour: (Int) -> Double = { minutes -> minutes / 60.0 }

    val max: (Int, Int) -> Int = { a, b -> Math.max(a, b) }

    val reverse: (String) -> String = { text -> text.reversed() }

    val minToMinHour: ((Int?) -> Pair<Int, Int>?) = { minutes ->
        minutes?.let { Pair(it / 60, it % 60) }
    }

    // Appel des lambdas
    lower("Coucou") // Affiche "coucou"
    println("126 minutes = ${hour(126)} heures") // Affiche "126 minutes = 2.1 heures"
    println("Max entre 4 et 9 : ${max(4, 9)}") // Affiche "Max entre 4 et 9 : 9"
    println("Reverse de 'toto' : ${reverse("toto")}") // Affiche "otot"

    // Test de minToMinHour
    println("126 minutes = ${minToMinHour(126)}") // Affiche "126 minutes = (2, 6)"
    println("null en entrée = ${minToMinHour(null)}") // Affiche "null en entrée = null"
}

fun exo2() {
    val compareUsersByName: (UserBean, UserBean) -> UserBean = { u1, u2 ->
        if (u1.name.lowercase() <= u2.name.lowercase()) u1 else u2
    }

    val compareUsersByOld: (UserBean, UserBean) -> UserBean = { u1, u2 ->
        if (u1.old >= u2.old) u1 else u2
    }

    val user1 = UserBean("Alice", 30)
    val user2 = UserBean("Bob", 25)

    println("Le nom le plus petit : ${compareUsersByName(user1, user2).name}")
    println("Le plus vieux : ${compareUsersByOld(user1, user2).name}")
}
