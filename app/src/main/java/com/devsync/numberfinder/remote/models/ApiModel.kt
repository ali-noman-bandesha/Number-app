package com.devsync.numberfinder.remote.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiModel
    (

    @SerializedName("valid")
    @Expose
    val valid: Boolean,
    @SerializedName("carrier")
    @Expose
    val carrier: String,


    )