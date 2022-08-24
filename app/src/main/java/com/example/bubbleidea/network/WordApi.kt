package com.example.bubbleidea.network

import com.example.bubbleidea.helpers.API_KEY
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

interface WordApi {
    @GET("search?apikey=$API_KEY")
    fun getWords(
        @Query("text") text : List<String>,
        @Query("lang") lang : String,
        @Query("type") type : String
    ) : Call<WordAssociationsApiResponse>
}