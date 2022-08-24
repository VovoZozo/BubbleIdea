package com.example.bubbleidea.repository

import com.example.bubbleidea.network.WordApi
import com.example.bubbleidea.network.WordAssociationsApiResponse
import retrofit2.Callback
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkWordListRepository @Inject constructor(private val wordApi: WordApi) {
    fun getWords(callback: Callback<WordAssociationsApiResponse>, text: List<String>, lang: String, type: String) {
        wordApi.getWords(text, lang, type).enqueue(callback)
    }
}