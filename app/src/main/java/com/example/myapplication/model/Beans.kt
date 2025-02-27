package com.amonteiro.a2025_02_supvinci_parisb.model


fun main() {

    var name : String?  = ""

    val randomName = RandomName()
    val randomName2= RandomName()


    randomName.add("bobby")
    randomName.addAll("bobby", "Tobby", "Gustavo")
    repeat(10) {
        println(randomName.next() + " ")
    }
    repeat(10) {
        print(randomName.nextDiff() + " ")
    }
    println("\n ${randomName.next2()}")

}

data class PictureBean(val id:Int, val url: String, val title: String, val longText: String)

class RandomName(){

    private val list = arrayListOf("Toto", "Tata", "bobby")
    private var oldValue = ""

    fun add(s: String) {
        if(s.isNotBlank() && s !in list) {
            list += s
        }
    }

    fun addAll(vararg  names: String) {
        for(name in names) {
            add(name)
        }

        //names.forEach { add(it)  }
    }

    fun next() = list.random()

    fun nextDiff(): String {
        var newValue = next()
        while(newValue == oldValue){
            newValue = next()
        }

        oldValue = newValue
        return oldValue
    }

    fun nextDiff2(): String {
        oldValue = list.filter { it != oldValue }.random()
        return oldValue
    }


    fun next2() = Pair(nextDiff(), nextDiff())

}


data class CarBean(var marque:String = "",var  model:String? = null){
    var color :String = ""
}