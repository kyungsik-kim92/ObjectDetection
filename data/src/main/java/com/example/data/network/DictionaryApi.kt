package com.example.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    companion object {
        private const val URL_DICTIONARY = "api/v2/entries/en/{word}"
    }

    @GET(URL_DICTIONARY)
    fun getDictionaryMean(
        @Path("word") word: String
    ): Call<com.example.data.network.response.DictionaryResponse>



}