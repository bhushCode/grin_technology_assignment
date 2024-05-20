package com.example.tesstingapplication02.service

import com.example.tesstingapplication02.ItemPage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("users?")
    fun getItems(@Query("page")page:Int):Call<ItemPage>
}