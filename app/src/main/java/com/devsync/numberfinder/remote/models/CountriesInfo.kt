package com.devsync.numberfinder.remote.models


data class CountriesInfo(

    val name: Name,
    val capital: List<String>?,
    val flags: Flags,
    val idd: Idd,
    val languages: Map<String, String>,
    val currencies: Map<String, Map<String, String>>,
    val population: Long?,
    val religion: String?,
    val policeNumber: String?,
    val fireBrigadeNumber: String?,
    val nationalAnimal: String?,
    val area: String?,
    val famousLandmark: String?,
    val latlng: List<Double>?,
    val flag: String,
    val car: Car,
    var isClicked: Boolean= false
) {
    @JvmName("setClickedFlag")
    fun setClicked(clicked: Boolean) {
        isClicked = clicked
    }
}

data class Name(
    val common: String?,
    val official: String?
)

data class Language(
    val common: String?,
    val official: String?
)

data class Flags(
    val svg: String?,
    val png: String?
)

data class Idd(
    val root: String,
    val suffixes: List<String>
)

data class Car(
    val signs: List<String>,
    val side: String
)

