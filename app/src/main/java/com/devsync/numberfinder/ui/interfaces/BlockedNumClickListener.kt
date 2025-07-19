package com.devsync.numberfinder.ui.interfaces

import com.devsync.numberfinder.remote.models.CountryDetails
import com.devsync.numberfinder.remote.models.NetworkProvider

interface BlockedNumClickListener
{
    fun onDataReceived(countryDetails : CountryDetails)
    fun onDataReceivedd(NetworkProvider : NetworkProvider)
}