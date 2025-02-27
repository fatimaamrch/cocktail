package com.example.myapplication.exo

fun boulangerie(
    croissants: Int = 0,
    baguettes: Int = 0,
    sandwichs: Int = 0
): Double = (croissants * CROISSANT_PRICE) + (baguettes * BAGUETTE_PRICE) + (sandwichs * SANDWICH_PRICE)

fun main() {
    // Appel avec seulement 2 croissants
    println("Total pour 2 croissants: ${boulangerie(croissants = 2)} euros")

    // Appel avec 2 baguettes et 3 sandwichs
    println("Total pour 2 baguettes et 3 sandwichs: ${boulangerie(baguettes = 2, sandwichs = 3)} euros")
}




