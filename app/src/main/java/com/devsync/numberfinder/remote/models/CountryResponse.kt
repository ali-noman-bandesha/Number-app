package com.devsync.numberfinder.remote.models


data class CountryResponse(
    val countries: List<CountryDetails>
)

data class CountryDetails(
    val name: String,
    val networkProviders: List<NetworkProvider>
)

data class NetworkProvider(
    val name: String,
    val helpline: String,
    val recharge: String,
    val balanceInquiry: String,
    val minutesInquiry: String,
    val mbsInquiry: String,
    val smsInquiry: String,
    val findMyNumber: String,
    val baseCode: String
)
