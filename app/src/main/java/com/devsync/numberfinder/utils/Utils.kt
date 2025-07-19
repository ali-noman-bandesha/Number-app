package com.devsync.numberfinder.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class Utils {

    fun checkInternet(context: Context): Boolean
    {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}