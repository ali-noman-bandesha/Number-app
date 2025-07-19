package com.devsync.numberfinder.remote.models

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.io.IOException
import java.util.Locale


class GoogleCoordinatesClass {


    //    provide phone number and default Region in the form of String and it will return country name
    fun getCountryNameFromPhoneNumber(phoneNumber: String, defaultRegion: String): String {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val parsedNumber: Phonenumber.PhoneNumber?

        try {
            parsedNumber = phoneNumberUtil.parse(phoneNumber, defaultRegion)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Invalid phone number"
        }

        val regionCode = phoneNumberUtil.getRegionCodeForNumber(parsedNumber)
        return if (regionCode != null) {
            val locale = Locale("", regionCode)
            locale.displayCountry
        } else {
            "Country not found"
        }
    }

    //    post location name and it will provide the coordinates in the form of 
    fun getCoordinatesFromLocationName(context: Context, locationName: String): android.location.Location?
    {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocationName(locationName, 1)

            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                return android.location.Location("").apply {
                    latitude = address.latitude
                    longitude = address.longitude
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
    fun getLocationName(latLng: LatLng, context: Context): String? {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                val sb = StringBuilder()

                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(", ")
                }
                sb.append(address.locality)
                return sb.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "Your location"
    }

    fun getExactLocationName(latLng: LatLng, context: Context): String? {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                val sb = StringBuilder()

                // Build the address using available information
                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(", ")
                }

                // Remove trailing comma and space
                sb.setLength(sb.length - 2)

                return sb.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }


}