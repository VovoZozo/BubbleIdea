package com.example.bubbleidea.di

import com.example.bubbleidea.network.WordApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideApi() : WordApi {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttp())
            .baseUrl(BASE_URL)
            .build()
        return retrofit.create(WordApi::class.java)
    }

    private fun getOkHttp() : OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    companion object {
        private const val BASE_URL = "https://api.wordassociations.net/associations/v1.0/json/"
    }
}