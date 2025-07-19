package com.devsync.numberfinder.remote.models

data class Country(
    val name: String,
    val policeNumber: String?,
    val majorReligion: String?,
    val fireBrigadeNumber: String?,
    val nationalAnimal: String?,
    val famousLandmark: String?,
    val currencyCode: String?
)