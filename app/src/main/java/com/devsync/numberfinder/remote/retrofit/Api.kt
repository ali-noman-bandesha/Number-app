package com.devsync.numberfinder.remote.retrofit

import com.devsync.numberfinder.remote.models.ApiModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api
{
    @GET("validate?")
    fun getData(
        @Query("access_key") accessKey: String,
        @Query("number") phoneNumber: String
    ):  Call<ApiModel>
}