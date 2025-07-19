package com.devsync.numberfinder.ui.interfaces

import com.devsync.numberfinder.remote.models.CountriesInfo

interface CountryClickListener {
    fun onDataReceived(countriesInfo: CountriesInfo)
    fun onBackPressed()
}