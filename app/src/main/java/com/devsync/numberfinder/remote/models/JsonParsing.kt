package com.devsync.numberfinder.remote.models



import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import javax.inject.Inject

class JsonParsing @Inject constructor() {
    fun loadJsonFromAssetsCountry(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun parseJsonDataCountry(jsonString: String): List<CountriesInfo> {
        val type = object : TypeToken<List<CountriesInfo>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
    fun parseJsonData(jsonString: String): List<Country> {
        val type = object : TypeToken<List<Country>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
    fun parseNetworkData(jsonString: String): List<CountryDetails> {
        val type = object : TypeToken<List<CountryDetails>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
}